package com.example.redsoportex.domain.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Lending {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_RETURNED = "RETURNED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    private final Long id;
    private final Long itemId;
    private final String itemTitle;
    private final Long requesterId;
    private final String requesterName;
    private final String requesterPhone;
    private final String donorPhone;
    private final String requestedAt;
    private final String startedAt;
    private final String expectedReturnAt;
    private final String actualReturnAt;
    private final String status;
    private final String handoverNotes;
    private final int days;

    public Lending(Long id, Long itemId, String itemTitle, Long requesterId,
                   String requesterName, String requesterPhone, String donorPhone,
                   String requestedAt, String startedAt, String expectedReturnAt,
                   String actualReturnAt, String status, String handoverNotes, int days) {
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
        this.days = days > 0 ? days : calculateDays(startedAt != null ? startedAt : requestedAt, expectedReturnAt);
    }

    public Long getId() {
        return id;
    }

    public Long getItemId() {
        return itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public String getRequesterPhone() {
        return requesterPhone;
    }

    public String getDonorPhone() {
        return donorPhone;
    }

    public String getRequestedAt() {
        return requestedAt;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public String getExpectedReturnAt() {
        return expectedReturnAt;
    }

    public String getActualReturnAt() {
        return actualReturnAt;
    }

    public String getStatus() {
        return status;
    }

    public String getHandoverNotes() {
        return handoverNotes;
    }

    public int getDays() {
        return days;
    }

    public boolean isPending() {
        return STATUS_PENDING.equalsIgnoreCase(status);
    }

    public boolean isActive() {
        return STATUS_ACTIVE.equalsIgnoreCase(status);
    }

    public boolean isReturned() {
        return STATUS_RETURNED.equalsIgnoreCase(status);
    }

    public boolean isCancelled() {
        return STATUS_CANCELLED.equalsIgnoreCase(status);
    }

    private static int calculateDays(String startStr, String endStr) {
        if (startStr == null || endStr == null) {
            return 14;
        }
        String[] patterns = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd"};
        Date start = null;
        Date end = null;
        for (String pattern : patterns) {
            if (start == null) {
                try {
                    start = new SimpleDateFormat(pattern, Locale.getDefault()).parse(startStr);
                } catch (ParseException ignored) {
                }
            }
            if (end == null) {
                try {
                    end = new SimpleDateFormat(pattern, Locale.getDefault()).parse(endStr);
                } catch (ParseException ignored) {
                }
            }
        }
        if (start != null && end != null) {
            long diffMillis = end.getTime() - start.getTime();
            if (diffMillis > 0) {
                return (int) Math.max(1, Math.round((double) diffMillis / (1000L * 60 * 60 * 24)));
            }
        }
        return 14;
    }
}
