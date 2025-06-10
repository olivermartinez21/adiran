package com.tmm.myre.base.configuration.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("mandREmptyContainerReleaseRepository")
public interface IMandREmptyContainerReleaseRepository extends JpaRepository<MandREmptyContainerReleaseModel, Integer>, JpaSpecificationExecutor<MandREmptyContainerReleaseModel> {
}
