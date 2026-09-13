package com.sunuhelp.notification.sender;

/**
 * Abstraction du canal SMS - MockSmsSender en dev (logge en console, cout
 * zero), un vrai fournisseur (Twilio, Africa's Talking...) en production.
 * Meme decision qu'on avait prise tres tot dans le projet : ne jamais
 * payer de vrais SMS pendant le developpement.
 */
public interface SmsSender {
    /** Retourne l'id externe du message si succes, leve une exception sinon. */
    String send(String phoneNumber, String content);
}
