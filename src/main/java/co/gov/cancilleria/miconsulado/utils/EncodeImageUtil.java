package co.gov.cancilleria.miconsulado.utils;

import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.IOUtils;

import com.gentics.mesh.rest.client.MeshBinaryResponse;

public class EncodeImageUtil {
	
	private EncodeImageUtil() {
	    throw new IllegalStateException("Utility class");
	}
	
	public static String getBase64ImageResource(MeshBinaryResponse binary) throws IOException {
		String imagenEncode = "";
		
		byte[] bytes = IOUtils.toByteArray(binary.getStream());
		Base64.Encoder encoder = Base64.getEncoder();
		imagenEncode = encoder.encodeToString(bytes);
		
		return imagenEncode;
	}

}
