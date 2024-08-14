package com.tmm.myre.base.configuration.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository("integrationRepository")
public interface IIntegrationRepository extends JpaRepository<IntegrationModel, Integer> {

}
