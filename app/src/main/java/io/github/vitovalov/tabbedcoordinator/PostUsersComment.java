package io.github.vitovalov.tabbedcoordinator;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by swapnil on 10/5/16.
 */

public class PostUsersComment extends AsyncTask<Void, Void, Void> {

    String url;
    Comments comment;

    public PostUsersComment(String url, Comments comment) {
            this.url = url;
            this.comment = comment;
    }

    @Override
    protected Void doInBackground(Void... params) {

        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpEntity<Comments> request = new HttpEntity<Comments>(comment);

        ResponseEntity<Comments> response = restTemplate.exchange(url, HttpMethod.POST, request, Comments.class);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        EventBusService.getInstance().post(comment);
    }
}
