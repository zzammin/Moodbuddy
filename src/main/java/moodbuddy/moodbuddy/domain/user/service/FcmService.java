package moodbuddy.moodbuddy.domain.user.service;

import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmReqDTO;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmResDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface FcmService {

    FcmResDTO sendMessageTo(FcmReqDTO fcmReqDTO);
}
