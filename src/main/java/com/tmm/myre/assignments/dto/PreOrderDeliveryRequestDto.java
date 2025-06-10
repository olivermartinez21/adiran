package com.tmm.myre.assignments.dto;

import com.tmm.myre.base.dto.AbstractManagement;
import com.tmm.myre.base.dto.ITransferObject;
import lombok.*;

@Getter
@Setter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PreOrderDeliveryRequestDto extends AbstractManagement implements ITransferObject {

    private static final long serialVersionUID = 1L;

    private String assignmentId;
    private String bookingOrder;
    private String shippingCompany;
    private String typeServiceOrder;
    private String billOrderTo;
    private String typeUnitOrder;
    private String carrierCompanyOrder;
    private String operatorOrder;
    private String economicNumberOrder;
    private String workOrderOrder;
}
