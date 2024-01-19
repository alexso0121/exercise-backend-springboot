package com.Acerise.System_api.dto.User;

import com.Acerise.System_api.Enum.SubscriptionPlanEnum;

public record ChangeSubRequest(
        String id,
        SubscriptionPlanEnum subscription_plan
) {
}
