package ru.vidtu.iasfork.msauth;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

public class Request {
	public HttpURLConnection conn;

	public Request(String url) throws Exception {
		this.conn = (HttpURLConnection) (new URL(url)).openConnection();
		this.conn.setDoOutput(true);
		this.conn.setDoInput(true);
	}

	public Request header(String key, String value) {
		this.conn.setRequestProperty(key, value);
		return this;
	}

	public Request post(String s) throws IOException {
		this.conn.setRequestMethod("POST");
		byte[] out = s.getBytes(StandardCharsets.UTF_8);
		this.conn.connect();
		OutputStream os = this.conn.getOutputStream();
		os.write(out);
		os.flush();
		os.close();
		return this;
	}

	public Request post(Map<?, ?> map) throws Exception {
		StringJoiner sj = new StringJoiner("&");

		for (Map.Entry<?, ?> entry : map.entrySet()) {
			sj.add(URLEncoder.encode(entry.getKey().toString(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
		}

		this.post(sj.toString());
		return this;
	}

	public Request get() throws ProtocolException {
		this.conn.setRequestMethod("GET");
		return this;
	}

	public int response() throws IOException {
		return this.conn.getResponseCode();
	}

	public String body() throws IOException {
		StringBuilder sb = new StringBuilder();
		Reader r = new InputStreamReader(this.conn.getInputStream(), StandardCharsets.UTF_8);

		int i;
		while ((i = r.read()) >= 0) {
			sb.append((char) i);
		}

		r.close();
		return sb.toString();
	}
}
