import com.echonest.api.v4.EchoNestException;


public class test {
	public static void main(String[] arag){
		
		try {
			MetaGrabber meta = new MetaGrabber(); // create the object
			meta.setTrackPath("test.mp3"); // set the path of the mp3
			meta.upload();
			while(meta.getUploadProgress().equals("pending")){
				// waits for the track to finish uploading, this can obviously be done in a better manner than this
				// possible options for getUploadProgress are pending, complete, error and unavailable
			}
			if(meta.getUploadProgress().equals("complete")){ // make sure there was no error
				meta.setMeta(); // call all the set methods to set the new data
				System.out.println("Artist: "+meta.getArtist()); // get the name of the artist
				System.out.println("Track: "+meta.getSongName()); // get the name of the song
				
				System.out.println("Dancebility: "+ meta.getDanceability()); // get the danceability of the song
				System.out.println("Speechyness: "+meta.getSpeechiness()); // get the speechiness
				
				System.out.println("Liveness: "+meta.getLiveness()); // liveness
				System.out.println("Energy: "+meta.getEnergy()); // energy
																	
				System.out.println("Duration: "+meta.getDuration()); // in seconds
				System.out.println("Loudness: "+meta.getLoudness()); //this can be minus, it means from a normalized point
				System.out.println("EndFade: "+meta.getEndFade()); //when the fade at the start ends
				System.out.println("StartFadeEnd: "+meta.getStartFade()); // when the fade at the end begins
				System.out.println("SongHotness: "+meta.getSongHotness()); 
				System.out.println("ArtistHotness: "+meta.getArtistHotness());
				
				System.out.println("Key Num: "+meta.getKeyInt()); // the key in its original form
				System.out.println("Key String: "+meta.getKeyString()); // changed into english
				System.out.println("Key Confidence: "+meta.getKeyConfidence()); // the confidence in that key
				
				System.out.println("Mode Num: "+meta.getModeInt()); // mode in orignial form
				System.out.println("Mode String: "+meta.getModeString());
				System.out.println("Mode Confidence: "+meta.getModeConfidence());
				

				System.out.println("Tempo: "+meta.getTempo());
				System.out.println("Tempo Confidence: "+meta.getTempoConfidence());
				System.out.println("Time Signature: "+meta.getTimeSignature());
				System.out.println("Time Signature Confidence: "+meta.getTimeSignatureConfidence());
				

				System.out.println("Location Name: "+meta.getArtistLocation());
				System.out.println("Longitude: "+meta.getArtistLongitude());
				System.out.println("Latitude: "+meta.getArtistLatitude());
				
				double[] bar1 = meta.getBar(1);
				double[] beat1 = meta.getBeat(1);
				double[] tatum1 = meta.getTatum(1);
				
				System.out.println("Bar 1: Bar start time " +bar1[0]+ " bar duration: " + bar1[1]+" bar confidence "+bar1[2]);
				System.out.println("Beat 1: Beat start time " +beat1[0]+ " beat duration: " + beat1[1]+" beat confidence "+beat1[2]);
				System.out.println("Tatum 1: Tatum start time " +tatum1[0]+ " tatum duration: " + tatum1[1]+" tatum confidence "+tatum1[2]);
				
			}
		} catch (EchoNestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
