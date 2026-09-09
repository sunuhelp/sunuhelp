package com.sunuhelp.entity.service.impl;

import com.sunuhelp.entity.dto.request.CreateServicePointRequest;
import com.sunuhelp.entity.dto.request.OpeningHourRequest;
import com.sunuhelp.entity.dto.request.SetOpeningHoursRequest;
import com.sunuhelp.entity.dto.request.SetTemporaryStatusRequest;
import com.sunuhelp.entity.dto.response.OpeningHoursResponse;
import com.sunuhelp.entity.dto.response.ServicePointResponse;
import com.sunuhelp.entity.entity.OpeningHours;
import com.sunuhelp.entity.entity.ServicePoint;
import com.sunuhelp.entity.enums.ServicePointType;
import com.sunuhelp.entity.exception.InvalidServicePointException;
import com.sunuhelp.entity.exception.ServicePointNotFoundException;
import com.sunuhelp.entity.i18n.MessageKeys;
import com.sunuhelp.entity.mapper.OpeningHoursMapper;
import com.sunuhelp.entity.mapper.OpeningStatusResolver;
import com.sunuhelp.entity.mapper.ServicePointMapper;
import com.sunuhelp.entity.repository.OpeningHoursRepository;
import com.sunuhelp.entity.repository.ServicePointRepository;
import com.sunuhelp.entity.service.EntityOwnershipValidator;
import com.sunuhelp.entity.service.ServicePointService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ServicePointServiceImpl implements ServicePointService {

    private final ServicePointRepository servicePointRepository;
    private final OpeningHoursRepository openingHoursRepository;
    private final EntityOwnershipValidator ownershipValidator;
    private final ServicePointMapper servicePointMapper;
    private final OpeningHoursMapper openingHoursMapper;
    private final OpeningStatusResolver openingStatusResolver;

    public ServicePointServiceImpl(ServicePointRepository servicePointRepository,
                                    OpeningHoursRepository openingHoursRepository,
                                    EntityOwnershipValidator ownershipValidator,
                                    ServicePointMapper servicePointMapper,
                                    OpeningHoursMapper openingHoursMapper,
                                    OpeningStatusResolver openingStatusResolver) {
        this.servicePointRepository = servicePointRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.ownershipValidator = ownershipValidator;
        this.servicePointMapper = servicePointMapper;
        this.openingHoursMapper = openingHoursMapper;
        this.openingStatusResolver = openingStatusResolver;
    }

    @Override
    @Transactional
    public ServicePointResponse create(UUID entityId, CreateServicePointRequest request, UUID requesterAccountId) {
        ownershipValidator.assertOwner(entityId, requesterAccountId);

        if (request.getType() == ServicePointType.PHYSICAL
                && (request.getAddress() == null || request.getAddress().isBlank())) {
            throw new InvalidServicePointException(MessageKeys.SERVICE_POINT_ADDRESS_REQUIRED);
        }
        if (request.getType() == ServicePointType.ONLINE
                && (request.getCoverageZone() == null || request.getCoverageZone().isBlank())) {
            throw new InvalidServicePointException(MessageKeys.SERVICE_POINT_COVERAGE_ZONE_REQUIRED);
        }

        ServicePoint point = ServicePoint.create(entityId, request.getName(), request.getType(),
                request.getAddress(), request.getCoverageZone());
        servicePointRepository.save(point);

        // Geocodage reel via geo-service a brancher plus tard (client HTTP entre services) -
        // point reste PENDING pour l'instant, publiable sans bloquer.
        return toResponse(point, List.of());
    }

    @Override
    public List<ServicePointResponse> findByEntity(UUID entityId) {
        return servicePointRepository.findByEntityIdAndActiveTrue(entityId).stream()
                .map(point -> toResponse(point, openingHoursRepository.findByServicePointId(point.getId())))
                .toList();
    }

    @Override
    @Transactional
    public void setTemporaryStatus(UUID servicePointId, SetTemporaryStatusRequest request, UUID requesterAccountId) {
        ServicePoint point = servicePointRepository.findById(servicePointId)
                .orElseThrow(ServicePointNotFoundException::new);
        ownershipValidator.assertOwner(point.getEntityId(), requesterAccountId);

        point.setTemporaryStatus(request.getStatus(), request.getUntil());
        servicePointRepository.save(point);
    }

    @Override
    @Transactional
    public List<OpeningHoursResponse> setOpeningHours(UUID servicePointId, SetOpeningHoursRequest request, UUID requesterAccountId) {
        ServicePoint point = servicePointRepository.findById(servicePointId)
                .orElseThrow(ServicePointNotFoundException::new);
        ownershipValidator.assertOwner(point.getEntityId(), requesterAccountId);

        openingHoursRepository.deleteAll(openingHoursRepository.findByServicePointId(servicePointId));

        List<OpeningHours> saved = request.getDays().stream()
                .map(d -> openingHoursRepository.save(OpeningHours.create(
                        servicePointId, d.getDayOfWeek(), d.isClosed(), d.getOpeningTime(), d.getClosingTime())))
                .toList();

        return saved.stream().map(openingHoursMapper::toResponse).toList();
    }

    private ServicePointResponse toResponse(ServicePoint point, List<OpeningHours> hours) {
        boolean currentlyOpen = openingStatusResolver.isCurrentlyOpen(point, hours);
        return servicePointMapper.toResponse(point, currentlyOpen);
    }
}
