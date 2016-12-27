package com.eusecom.attendance.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START Poll_class]
@IgnoreExtraProperties
public class Event {
        private String title, desc, image;

        public Event(String title, String desc, String image) {
            this.title = title;
            this.desc = desc;
            this.image = image;
        }
        public Event(){

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("desc", desc);
        result.put("image", image);

        return result;
    }


    }