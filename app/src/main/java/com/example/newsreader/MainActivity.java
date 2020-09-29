package com.example.newsreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.system.StructTimespec;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recViewContent;
    private ArrayList<NewsItems> news;
    private NewsAdaptor adaptor;
    private final String TAG = "shit------>>>>>>>>>>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recViewContent = findViewById(R.id.RecViewContent);
        news = new ArrayList<>();

        adaptor = new NewsAdaptor(this);
        new getNews().execute();
        recViewContent.setAdapter(adaptor);
        recViewContent.setLayoutManager(new LinearLayoutManager(this));

    }

    private class getNews extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            InputStream inputStream = getInputStreams();
            if (null != inputStream) {
                Log.d(TAG, "doInBackground: shit we are here now inputStream no problem");
                try {
                    initXMLPullParser(inputStream);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "doInBackground: shit you failed again here no connection made.");
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute: " + news);
            adaptor.setNews(news);
        }

        private InputStream getInputStreams()  {
            try {
                URL url = new URL("https://www.autosport.com/rss/feed/f1");
                Log.d(TAG, "getInputStream: " + url.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                return connection.getInputStream();
            } catch (IOException e) {
                Log.d(TAG, "getInputStreams: lol you just failed.!!");
                e.printStackTrace();
            }
            return null;
        }

        private void initXMLPullParser(InputStream inputStream) throws XmlPullParserException, IOException {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(inputStream,null);
            parser.next();

            parser.require(XmlPullParser.START_TAG,null,"rss");
            while (parser.next() != XmlPullParser.END_TAG) {
                if(parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                parser.require(XmlPullParser.START_TAG,null,"channel");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }

                    if(parser.getName().equals("item")) {
                        parser.require(XmlPullParser.START_TAG, null, "item");

                        String title = "";
                        String description = "";
                        String link = "";
                        String date = "";

                        while (parser.next() != XmlPullParser.END_TAG) {
                            if (parser.getEventType() != XmlPullParser.START_TAG) {
                                continue;
                            }

                            String tagName = parser.getName();
                            if(tagName.equals("title")) {
                                title = getContent(parser,"title");
                            }else if(tagName.equals("description")){
                                description = getContent(parser,"description");
                            }else if(tagName.equals("link")){
                                link = getContent(parser,"link");
                            }else if(tagName.equals("pubDate")){
                                date = getContent(parser,"pubDate");
                            }else {
                                skipTag(parser);
                            }
                        }

                        NewsItems item = new NewsItems(title,description,link,date);
                        Log.d(TAG, "initXMLPullParser: testing out the news item we have made>>>>>>> " + item);
                        news.add(item);

                    }else {
                        skipTag(parser);
                    }
                }
            }
        }

        private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            Log.d(TAG, "skipTag: " + parser.getName());
            int number = 1;
            while (number != 0) {
                switch (parser.next()) {
                    case XmlPullParser.START_TAG:
                        number++;
                        break;
                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "skipTag: " + parser.getName());
                        number--;
                        break;
                    default:
                        break;
                }
            }

        }

        private String getContent (XmlPullParser parser, String TagName) throws IOException, XmlPullParserException {

            String content = "";
            parser.require(XmlPullParser.START_TAG, null, TagName);

            if(parser.next() == XmlPullParser.TEXT){
                content = parser.getText();
                parser.next();
            }
            return content;
        }
    }



}