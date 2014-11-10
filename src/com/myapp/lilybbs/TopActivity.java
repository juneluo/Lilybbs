package com.myapp.lilybbs;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TopActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top);
		new DownloadTask().execute("http://bbs.nju.edu.cn/bbstop10");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.top, menu);
		return true;
	}

	public class DownloadTask extends AsyncTask<String, Void, String[]> {

		
		@Override
		protected String[] doInBackground(String... urls) {
			try {
				return parseTop10();
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * Uses the logging framework to display the output of the fetch
		 * operation in the log fragment.
		 */
		@Override
		protected void onPostExecute(String[] results) {
			// Log.i(TAG, result);
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(TopActivity.this,
			        android.R.layout.simple_list_item_1, results);
			
			setListAdapter(adapter);
			
		}
	}

	/** Initiates the fetch operation. */
	private String loadFromNetwork(String urlString) throws IOException {
		InputStream stream = null;
		String str = "";

		try {
			stream = downloadUrl(urlString);
			str = readIt(stream, 5000);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		return str;
	}

	/**
	 * Given a string representation of a URL, sets up a connection and gets an
	 * input stream.
	 * 
	 * @param urlString
	 *            A string representation of a URL.
	 * @return An InputStream retrieved from a successful HttpURLConnection.
	 * @throws java.io.IOException
	 */
	private InputStream downloadUrl(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// Start the query
		conn.connect();
		InputStream stream = conn.getInputStream();
		return stream;
	}

	/**
	 * Reads an InputStream and converts it to a String.
	 * 
	 * @param stream
	 *            InputStream containing HTML from targeted site.
	 * @param len
	 *            Length of string that this method returns.
	 * @return String concatenated according to len parameter.
	 * @throws java.io.IOException
	 * @throws java.io.UnsupportedEncodingException
	 */
	private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {

		int buffsize = 500;
		StringBuilder out = new StringBuilder();

		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		char[] buffer = new char[buffsize];

		try {
			for (;;) {
				int rsz = reader.read(buffer, 0, buffer.length);
				if (rsz < 0)
					break;
				out.append(buffer, 0, rsz);
			}
		} finally {
			reader.close();
		}

		return out.toString();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private String[] parseTop10() throws ParserException {
		final String DW_HOME_PAGE_URL = "http://bbs.nju.edu.cn/bbstop10";
		ArrayList<String> pTitleList = new ArrayList<String>();
		
		Parser htmlParser = new Parser(DW_HOME_PAGE_URL);
		htmlParser.setEncoding("UTF-8");
		String postTitle = "";
		
		NodeList toptable = htmlParser.extractAllNodesThatMatch(new AndFilter(new NodeClassFilter(TableTag.class),
				new HasAttributeFilter("width", "640")));

		if (toptable != null && toptable.size() > 0) {
			
			NodeList itemTopList = toptable
					.elementAt(0)
					.getChildren()
					.extractAllNodesThatMatch(
							(new NodeClassFilter(TableRow.class)), true);

			if (itemTopList != null && itemTopList.size() > 0) {
				for (int i = 0; i < itemTopList.size()-1; ++i) {
				
					NodeList linkItem = itemTopList.elementAt(i).getChildren()
							.extractAllNodesThatMatch(new NodeClassFilter(TableColumn.class), true);
					if (linkItem != null && linkItem.size() > 0) {
					
						postTitle = ((LinkTag)((linkItem.elementAt(7)).getChildren().elementAt(0))).getLinkText();
						System.out.println(postTitle);
						pTitleList.add(postTitle);
					}
				}
			}
		}
		String[] results=new String[pTitleList.size()];
		pTitleList.toArray(results);
		return results;
	}

}

