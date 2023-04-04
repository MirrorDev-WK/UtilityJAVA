package th.co.ncr.connector.utils;

import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Component
public class Utils{
	private static final int BUFFER_LENGTH = (128 * 1024);
	public static byte[] doGzip(byte[] input) {
		byte[] buffer = new byte[BUFFER_LENGTH];
		byte[] rsByte = null;
		int cnt;

		try (
				InputStream byteIn = new ByteArrayInputStream(input);
				ByteArrayOutputStream bos  = new ByteArrayOutputStream(byteIn.available());
				GZIPOutputStream gzipOuputStream = new GZIPOutputStream(bos);
		) {

			while ((cnt = byteIn.read(buffer)) > 0) {
				gzipOuputStream.write(buffer, 0, cnt);
			}
			gzipOuputStream.finish();
			rsByte = bos.toByteArray();

		} catch (Exception ex) {
		}

		return rsByte;
	}

	public static byte[] doGunzip(byte[] input) {
		byte[] buffer = new byte[BUFFER_LENGTH];
		byte[] rsByte = null;
		int cnt;

		try (
				InputStream byteIn = new ByteArrayInputStream(input);
				GZIPInputStream gZIPInputStream = new GZIPInputStream(byteIn);
				ByteArrayOutputStream bos  = new ByteArrayOutputStream(byteIn.available());
		) {

			while ((cnt = gZIPInputStream.read(buffer)) > 0) {
				bos.write(buffer, 0, cnt);
			}
			rsByte = bos.toByteArray();

		} catch (Exception ex) {
		}

		return rsByte;
	}
}