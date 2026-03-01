-keepnames class com.squires.gamechanger.network.dto.TeamDto
-if class com.squires.gamechanger.network.dto.TeamDto
-keep class com.squires.gamechanger.network.dto.TeamDtoJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
