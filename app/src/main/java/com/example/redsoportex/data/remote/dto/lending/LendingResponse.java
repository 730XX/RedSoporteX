package com.example.redsoportex.data.remote.dto.lending;

import com.google.gson.annotations.SerializedName;

public class LendingResponse {

    @SerializedName("id")
    private Long id;

    @SerializedName("itemId")
    private Long itemId;

    @SerializedName("itemTitle")
    private String itemTitle;

    @SerializedName("requesterId")
    private Long requesterId;

    @SerializedName("requesterName")
    private String requesterName;

    @SerializedName("requesterPhone")
    private String requesterPhone;

    @SerializedName("donorPhone")
    private String donorPhone;

    @SerializedName("requestedAt")
    private String requestedAt;

    @SerializedName("startedAt")
    private String startedAt;

    @SerializedName("expectedReturnAt")
    private String expectedReturnAt;

    @SerializedName("actualReturnAt")
    private String actualReturnAt;

    @SerializedName("status")
    private String status;

    @SerializedName("handoverNotes")
    private String handoverNotes;

    public LendingResponse() {
    }

    public LendingResponse(Long id, Long itemId, String itemTitle, Long requesterId,
                           String requesterName, String requesterPhone, String donorPhone,
                           String requestedAt, String startedAt, String expectedReturnAt,
                           String actualReturnAt, String status, String handoverNotes) {
        this.id = id;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.requesterId = requesterId;
        this.requesterName = requesterName;
        this.requesterPhone = requesterPhone;
        this.donorPhone = donorPhone;
        this.requestedAt = requestedAt;
        this.startedAt = startedAt;
        this.expectedReturnAt = expectedReturnAt;
        this.actualReturnAt = actualReturnAt;
        this.status = status;
        this.handoverNotes = handoverNotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterPhone() {
        return requesterPhone;
    }

    public void setRequesterPhone(String requesterPhone) {
        this.requesterPhone = requesterPhone;
    }

    public String getDonorPhone() {
        return donorPhone;
    }

    public void setDonorPhone(String donorPhone) {
        this.donorPhone = donorPhone;
    }

    public String getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(String requestedAt) {
        this.requestedAt = requestedAt;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getExpectedReturnAt() {
        return expectedReturnAt;
    }

    public void setExpectedReturnAt(String expectedReturnAt) {
        this.expectedReturnAt = expectedReturnAt;
    }

    public String getActualReturnAt() {
        return actualReturnAt;
    }

    public void setActualReturnAt(String actualReturnAt) {
        this.actualReturnAt = actualReturnAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHandoverNotes() {
        return handoverNotes;
    }

    public void setHandoverNotes(String handoverNotes) {
        this.handoverNotes = handoverNotes;
    }
}
