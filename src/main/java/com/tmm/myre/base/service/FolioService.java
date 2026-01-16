package com.tmm.myre.base.service;

import com.tmm.myre.base.repository.ICounterRepository;
import com.tmm.myre.base.service.core.IFolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("folioService")
public class FolioService implements IFolioService {

    @Autowired
    private ICounterRepository counterRepository;

    /*public FolioService(ICounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }*/


    @Override
    public long next(String counterName) {
        // 1) incrementa el contador (EIR-IN, EIR-OUT, ESTIMADO, etc.)
        counterRepository.increment(counterName);

        // 2) obtiene el nuevo valor
        Long value = counterRepository.getLastInsertId();
        if (value == null) {
            throw new IllegalStateException("No se pudo obtener LAST_INSERT_ID() para " + counterName);
        }
        return value;
    }
    @Override
    public long nextEirIn() {
        return next("EIR-IN");
    }
    @Override
    public long nextEirOut() {
        return next("EIR-OUT");
    }

    @Override
    public long nextEstimado() {
        return next("ESTIMADO");
    }
}

