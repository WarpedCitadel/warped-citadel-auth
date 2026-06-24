package com.warpedcitadel.warpedcitadelauth.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditRepository auditRepository;

    public List<String> getAppUserSessions(String uuid){
        return auditRepository.getAppUserSessions(uuid);
    }
}
