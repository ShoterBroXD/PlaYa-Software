package com.playa.dto;

import lombok.Data;

@Data
public class NotificationPreferenceRequestDto {
    private Boolean enabledComments=true;
    private Boolean enableSystems=true;
    private Boolean enableNewReleases=true;
    private Boolean enableFollowers=true;
}
