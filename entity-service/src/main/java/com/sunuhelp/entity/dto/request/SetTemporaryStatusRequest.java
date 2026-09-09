package com.sunuhelp.entity.dto.request;

import com.sunuhelp.entity.enums.TemporaryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetTemporaryStatusRequest {

    @NotNull
    private TemporaryStatus status;

    /** Date a partir de laquelle le statut redevient normal automatiquement. */
    private LocalDateTime until;
}
