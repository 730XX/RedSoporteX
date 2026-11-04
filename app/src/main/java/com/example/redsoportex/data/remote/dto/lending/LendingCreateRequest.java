package com.example.redsoportex.data.remote.dto.lending;

import com.google.gson.annotations.SerializedName;

public class LendingCreateRequest {

    @SerializedName("itemId")
    private Long itemId;

    @SerializedName("expectedReturnDays")
    private Integer expectedReturnDays;

    @SerializedName("handoverNotes")
    private String handoverNotes;

    public LendingCreateRequest() {
    }

    public LendingCreateRequest(Long itemId, Integer expectedReturnDays, String handoverNotes) {
        this.itemId = itemId;
        this.expectedReturnDays = expectedReturnDays;
        this.handoverNotes = handoverNotes;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getExpectedReturnDays() {
        return expectedReturnDays;
    }

    public void setExpectedReturnDays(Integer expectedReturnDays) {
        this.expectedReturnDays = expectedReturnDays;
    }

    public String getHandoverNotes() {
        return handoverNotes;
    }

    public void setHandoverNotes(String handoverNotes) {
        this.handoverNotes = handoverNotes;
    }
}
