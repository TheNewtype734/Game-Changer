-keepnames class com.squires.gamechanger.network.dto.LeagueDto
-if class com.squires.gamechanger.network.dto.LeagueDto
-keep class com.squires.gamechanger.network.dto.LeagueDtoJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
