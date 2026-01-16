package com.tmm.myre.base.service.core;

public interface IFolioService {

    long next(String counterName);
    long nextEirIn();
    long nextEirOut();
    long nextEstimado();
}
