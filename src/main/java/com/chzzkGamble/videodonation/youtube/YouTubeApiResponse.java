package com.chzzkGamble.videodonation.youtube;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.text.StringEscapeUtils;
import java.util.List;

public record YouTubeApiResponse(
        String kind,
        String etag,
        @JsonProperty("nextPageToken") String nextPageToken,
        @JsonProperty("regionCode") String regionCode,
        PageInfo pageInfo,
        List<Item> items
) {

    public record PageInfo(
            int totalResults,
            int resultsPerPage
    ) {
    }

    public record Item(
            String kind,
            String etag,
            Id id,
            Snippet snippet
    ) {
        public record Id(
                String kind,
                String videoId
        ) {
        }

        public record Snippet(
                String publishedAt,
                String channelId,
                String title,
                String description,
                Thumbnails thumbnails,
                String channelTitle,
                String liveBroadcastContent,
                @JsonProperty("publishTime") String publishTime
        ) {

            public Snippet {
                title = StringEscapeUtils.unescapeHtml4(title);
            }

            public record Thumbnails(
                    Thumbnail defaultThumbnail,
                    Thumbnail medium,
                    Thumbnail high
            ) {
                public record Thumbnail(
                        String url,
                        int width,
                        int height
                ) {
                }
            }
        }
    }
}
