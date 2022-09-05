package com.lmc.autotest.api.model.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerInfoVo {

	@ApiModelProperty("日订单额")
	private BigDecimal dayAvgAmount;

	@ApiModelProperty("客单价")
	private BigDecimal orderAvgAmount;

	@ApiModelProperty("月频次")
	private Integer orderCount;

	@ApiModelProperty("未下单天数")
	private Integer noOrderDays;

	@ApiModelProperty("距离")
	private Integer distance;
	
	private Long id;

    private String customerNumber;

    private String customerName;

    private String firstCategoryCode;

    private String secondCategoryCode;

    private String thirdCategoryCode;

    private Integer customerCategoryId;

    private String archiveCategory;

    private String cooperationMode;

    private String socialCreditCode;

    private String businessLicense;

    private String legalPersonName;

    private String legalPersonIdcard;

    private String registeredCapital;

    private String phone;

    private String contactPerson;

    private String email;

    private String fax;

    private String countryCode;

    private String postCode;

    private String invoiceName;

    private String invoiceName2;

    private String regionProvince;

    private String regionProvinceName;

    private String regionCity;

    private String regionArea;

    private String addressDetails;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String bankAccountNumber;

    private String bankAccountName;

    private String bankName;

    private String bankNumber;

    private String bankAccountNumber2;

    private String companyCode;

    private String paymentTerms;

    private String payMode;

    private String paymentDays;

    private BigDecimal planSalesAmount;

    private BigDecimal writtenAmount;

    private String b2bShopCode;

    private String b2bShopName;

    private Long salesUserId;

    private String salesUserName;

    private String taxesCategory;

    private String salesProvince;

    private String customerLevel;

    private BigDecimal creditLimit;

    private BigDecimal tempCreditLimit;

    private Date tempBeginTime;

    private Date tempEndTime;

    private Integer creditModulus;

    private String customerStatus;

    private Boolean activeFlag;

    private Boolean syncFlag;

    private String channelFirstCode;

    private String channelSecondCode;

    private String saleOrganization;

    private String distributionChannel;

    private String saleRegion;

    private String creditRange;

    private String remark;

    private Date contractBeginTime;

    private Date contractEndTime;

    private String customerNature;

    private String invoiceAddress;

    private String invoiceAddressDetails;

    private Boolean haveThirdPartyCreditFile;

    private Integer customerNatureId;

    private Integer archiveCategoryId;

    private Integer registeredCapitalId;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private String filePath;
}
