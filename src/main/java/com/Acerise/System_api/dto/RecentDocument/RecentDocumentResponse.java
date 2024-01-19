package com.Acerise.System_api.dto.RecentDocument;

import com.Acerise.System_api.Model.Exercise;

public record RecentDocumentResponse(
        String recentDocument_id,
        Exercise exercise

) {
}
