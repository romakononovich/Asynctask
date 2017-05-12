package xyz.romakononovich.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private View frameLayout;
    private TextView textView;
    private List<Article> articleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.frameLayout);
        textView = (TextView) findViewById(R.id.textView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request.Builder builder = new Request.Builder();
        builder.url("https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=f4d21a8d9a1e496081d03f1d83fbc88f");
        Request request = builder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "call: "+ call.toString()+"error: "+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               // Log.d("TAG", "code: "+response.body());
                //Log.d("TAG", "response: "+response.body().string());

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.d("TAG", "source: "+ jsonObject.get("source"));
                    JSONArray articles = jsonObject.getJSONArray("articles");
                    for (int i = 0; i<articles.length(); i++) {
                        JSONObject jsonObject1 = new JSONObject(articles.get(i).toString());
                        Article article = new Article();
                        article.setAuthor(jsonObject1.getString("author"));
                        article.setDescription(jsonObject1.getString("description"));
                        article.setPublishedAt(jsonObject1.getString("publishedAt"));
                        article.setTitle(jsonObject1.getString("title"));
                        article.setUrl(jsonObject1.getString("url"));
                        article.setUrlToImage(jsonObject1.getString("urlToImage"));
                        Log.d("TAG", "article" + article.toString());
                        articleList.add(article);
                    }
                    setArticles();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

//        Log.d("TAG", "Resume:" + Thread.currentThread().getName());
//        new MyAsyncTask("передано", this).execute();
    }

    public void showProgressBar() {
        frameLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        frameLayout.setVisibility(View.GONE);
    }
    public void onUserRecived(final User user) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String current = textView.getText().toString();
                current = current + "\n" + user.name;
                textView.setText(current);
            }
        });

    }

    static class User {
        String name;
        int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
    private void setArticles() {
        StringBuilder stringBuilder = new StringBuilder(articleList.size());
        for (Article article: articleList) {
            stringBuilder.append(article.toString());
        }
        textView.setText(stringBuilder.toString());

    }
}
