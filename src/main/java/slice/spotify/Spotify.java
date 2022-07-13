//package slice.spotify;
//
//import lombok.Getter;
//import lombok.Setter;
//import se.michaelthelin.spotify.SpotifyApi;
//
//@Getter @Setter
//public class Spotify {
//    private final String apikey;
//    private final SpotifyApi spotifyApi;
//
//    public Spotify(String apikey) {
//        this.apikey = apikey;
//
//        this.spotifyApi = new SpotifyApi.Builder()
//                .setAccessToken(apikey)
//                .build();
//        this.getSomethingRequest = this.spotifyApi.getSomething();
//                .market(CountryCode.SE)
//                .build();
//    }
//}
