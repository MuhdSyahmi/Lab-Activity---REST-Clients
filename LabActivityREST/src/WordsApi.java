import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class WordsApi {

	private JFrame frame;
	private JTextField textField_word;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WordsApi window = new WordsApi();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WordsApi() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 584, 404);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblWords = new JLabel("Words: ");
		lblWords.setBounds(41, 40, 56, 16);
		frame.getContentPane().add(lblWords);
		
		textField_word = new JTextField();
		textField_word.setBounds(94, 37, 116, 22);
		frame.getContentPane().add(textField_word);
		textField_word.setColumns(10);
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setBounds(94, 115, 395, 123);
		frame.getContentPane().add(textArea);
		
		JButton btnSearch = new JButton("SEARCH");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread = new Thread(){
					public void run(){
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("query", textField_word.getText()));
						
						String strUrl = "http://apilayer.net/api/detect?access_key=931d455abc86aa8c061d8f6098ebe07f";
					
						JSONObject jsonObj = makeHttpRequest(strUrl,"POST",params);
						try
						{
							JSONArray jsonarray = jsonObj.getJSONArray("results");
							
							for (int i = 0; i < jsonarray.length(); i++) 
							{
								String code = jsonarray.getJSONObject(i).getString("language_code");
							    String name = jsonarray.getJSONObject(i).getString("language_name");
							    String strSetText = "Language Code :"+code+" || Language Name : "+name;
							    textArea.setText(strSetText);
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
						
			}
					public JSONObject makeHttpRequest(String strUrl, String method, List<NameValuePair> params) {
						InputStream is = null;
						String json = "";
						JSONObject jObj = null;
						
						try {
							if(method == "POST") {
								DefaultHttpClient httpClient = new DefaultHttpClient();
								HttpPost httpPost = new HttpPost(strUrl);
								httpPost.setEntity(new UrlEncodedFormEntity(params));
								HttpResponse httpResponse = httpClient.execute(httpPost);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							else if(method == "GET") {
								DefaultHttpClient httpClient = new DefaultHttpClient();
								String paramString = URLEncodedUtils.format(params, "utf-8");
								strUrl+="?"+paramString;
								HttpGet httpGet = new HttpGet(strUrl);
								
								HttpResponse httpResponse = httpClient.execute(httpGet);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							
							BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
							StringBuilder sb = new StringBuilder();
							String line = null;
							while((line = reader.readLine())!=null) 
								sb.append(line+"\n");
							is.close();
							json = sb.toString();
							jObj = new JSONObject(json);
							
						}	catch(JSONException e) {
							try {
								JSONArray jArr = new JSONArray(json);
							}catch(JSONException e1) {
								e1.printStackTrace();
							}
						}	catch (Exception ee) {
							ee.printStackTrace();
						}
						return jObj;
					}
				};
				thread.start();
			}
		});
		btnSearch.setBounds(104, 73, 97, 25);
		frame.getContentPane().add(btnSearch);
	}
}
