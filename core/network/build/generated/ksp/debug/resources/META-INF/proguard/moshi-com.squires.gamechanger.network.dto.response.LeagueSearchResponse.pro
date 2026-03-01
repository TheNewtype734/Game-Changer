-keepnames class com.squires.gamechanger.network.dto.response.LeagueSearchResponse
-if class com.squires.gamechanger.network.dto.response.LeagueSearchResponse
-keep class com.squires.gamechanger.network.dto.response.LeagueSearchResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
