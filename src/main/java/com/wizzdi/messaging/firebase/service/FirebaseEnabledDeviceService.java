package com.wizzdi.messaging.firebase.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.SoftDeleteOption;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.messaging.connectors.firebase.model.FirebaseEnabledDevice;
import com.wizzdi.messaging.firebase.data.FirebaseEnabledDeviceRepository;
import com.wizzdi.messaging.firebase.request.FirebaseEnabledDeviceCreate;
import com.wizzdi.messaging.firebase.request.FirebaseEnabledDeviceFilter;
import com.wizzdi.messaging.firebase.request.FirebaseEnabledDeviceUpdate;
import com.wizzdi.messaging.model.MessageReceiverDevice;
import com.wizzdi.messaging.request.MessageReceiverDeviceCreate;
import com.wizzdi.messaging.request.MessageReceiverDeviceFilter;
import com.wizzdi.messaging.service.MessageReceiverDeviceService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Extension
@Component
public class FirebaseEnabledDeviceService implements Plugin {


    @Autowired
    private FirebaseEnabledDeviceRepository firebaseEnabledDeviceRepository;
    @Autowired
    private MessageReceiverDeviceService messageReceiverDeviceService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public FirebaseEnabledDevice createFirebaseEnabledDevice(FirebaseEnabledDeviceCreate firebaseEnabledDeviceCreate, SecurityContextBase securityContext) {
        FirebaseEnabledDevice firebaseEnabledDevice = createFirebaseEnabledDeviceNoMerge(firebaseEnabledDeviceCreate, securityContext);
        firebaseEnabledDeviceRepository.merge(firebaseEnabledDevice);
        return firebaseEnabledDevice;
    }

    public FirebaseEnabledDevice getOrCreateFirebaseEnabledDevice(FirebaseEnabledDeviceCreate firebaseEnabledDeviceCreate, SecurityContextBase securityContext) {
        FirebaseEnabledDevice firebaseEnabledDevice = listAllFirebaseEnabledDevices(new FirebaseEnabledDeviceFilter().setChatUsers(Collections.singletonList(firebaseEnabledDeviceCreate.getOwner())).setBasicPropertiesFilter(new BasicPropertiesFilter().setSoftDelete(SoftDeleteOption.BOTH)).setExternalIds(Collections.singleton(firebaseEnabledDeviceCreate.getExternalId())), securityContext).stream().findFirst().orElse(null);
        if (firebaseEnabledDevice == null) {
            firebaseEnabledDevice = createFirebaseEnabledDevice(firebaseEnabledDeviceCreate, securityContext);
        } else {
            firebaseEnabledDeviceCreate.setSoftDelete(false);
            if (updateFirebaseEnabledDeviceNoMerge(firebaseEnabledDeviceCreate, firebaseEnabledDevice)) {
                merge(firebaseEnabledDevice);
            }
        }
        return firebaseEnabledDevice;
    }

    public FirebaseEnabledDevice createFirebaseEnabledDeviceNoMerge(FirebaseEnabledDeviceCreate firebaseEnabledDeviceCreate, SecurityContextBase securityContext) {
        FirebaseEnabledDevice firebaseEnabledDevice = new FirebaseEnabledDevice();
        firebaseEnabledDevice.setId(Baseclass.getBase64ID());
        updateFirebaseEnabledDeviceNoMerge(firebaseEnabledDeviceCreate, firebaseEnabledDevice);
        return firebaseEnabledDevice;
    }

    public boolean updateFirebaseEnabledDeviceNoMerge(FirebaseEnabledDeviceCreate firebaseEnabledDeviceCreate, FirebaseEnabledDevice firebaseEnabledDevice) {
        boolean update = messageReceiverDeviceService.updateMessageReceiverDeviceNoMerge(firebaseEnabledDeviceCreate, firebaseEnabledDevice);
        return update;
    }

    public FirebaseEnabledDevice updateFirebaseEnabledDevice(FirebaseEnabledDeviceUpdate firebaseEnabledDeviceUpdate, SecurityContextBase securityContext) {
        FirebaseEnabledDevice firebaseEnabledDevice = firebaseEnabledDeviceUpdate.getFirebaseEnabledDevice();
        if (updateFirebaseEnabledDeviceNoMerge(firebaseEnabledDeviceUpdate, firebaseEnabledDevice)) {
            firebaseEnabledDeviceRepository.merge(firebaseEnabledDevice);

        }
        return firebaseEnabledDevice;
    }

    public void validate(FirebaseEnabledDeviceCreate firebaseEnabledDeviceCreate, SecurityContextBase securityContext) {
        messageReceiverDeviceService.validate(firebaseEnabledDeviceCreate, securityContext);

    }

    public void validate(FirebaseEnabledDeviceFilter firebaseEnabledDeviceFilter, SecurityContextBase securityContext) {
        messageReceiverDeviceService.validate(firebaseEnabledDeviceFilter, securityContext);


    }



    public PaginationResponse<FirebaseEnabledDevice> getAllFirebaseEnabledDevices(FirebaseEnabledDeviceFilter FirebaseEnabledDeviceFilter, SecurityContextBase securityContext) {
        List<FirebaseEnabledDevice> list = listAllFirebaseEnabledDevices(FirebaseEnabledDeviceFilter, securityContext);
        long count = firebaseEnabledDeviceRepository.countAllFirebaseEnabledDevices(FirebaseEnabledDeviceFilter, securityContext);
        return new PaginationResponse<>(list, FirebaseEnabledDeviceFilter, count);
    }

    public List<FirebaseEnabledDevice> listAllFirebaseEnabledDevices(FirebaseEnabledDeviceFilter FirebaseEnabledDeviceFilter, SecurityContextBase securityContext) {
        return firebaseEnabledDeviceRepository.listAllFirebaseEnabledDevices(FirebaseEnabledDeviceFilter, securityContext);
    }

    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
        return firebaseEnabledDeviceRepository.listByIds(c, ids, securityContext);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
        return firebaseEnabledDeviceRepository.getByIdOrNull(id, c, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return firebaseEnabledDeviceRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return firebaseEnabledDeviceRepository.listByIds(c, ids, baseclassAttribute, securityContext);
    }

    public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
        return firebaseEnabledDeviceRepository.findByIds(c, ids, idAttribute);
    }

    public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
        return firebaseEnabledDeviceRepository.findByIds(c, requested);
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        return firebaseEnabledDeviceRepository.findByIdOrNull(type, id);
    }

    @Transactional
    public void merge(Object base) {
        firebaseEnabledDeviceRepository.merge(base);
    }

    @Transactional
    public void massMerge(List<?> toMerge) {
        firebaseEnabledDeviceRepository.massMerge(toMerge);
    }
}
