package com.Acerise.System_api.dto.User;

import com.Acerise.System_api.Enum.Grade;
import com.Acerise.System_api.Enum.Subject;
import com.Acerise.System_api.Enum.SubscriptionPlanEnum;
import com.Acerise.System_api.Model.Tags;

import java.util.List;

public record UserUpdateRequest(
        String id,
        String username,

        String icon_file_name,

        String sign_up_location,

        List<Subject> prefer_subj,

        List<Grade> prefer_grade,

        List<Tags> prefer_tag
) {
}
