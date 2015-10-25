package com.rtong.instagramclient;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    // what our item looks like
    // use the template to display each photo

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for this position
        InstagramPhoto photo = getItem(position);
        // check if we are using a recycled view, if not we need to inflate
        if(convertView == null){
            //create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // lookup the views for populating the data (image, caption)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvCommentCount = (TextView) convertView.findViewById(R.id.tvCommentCount);
        TextView tvComment1 = (TextView) convertView.findViewById(R.id.tvComment1);
        TextView tvComment2 = (TextView) convertView.findViewById(R.id.tvComment2);

        // insert the model data into each of the view items
        tvCaption.setText(photo.caption);
        tvUsername.setText(photo.username);
        tvLikes.setText(photo.likesCount + " likes");

        tvCommentCount.setText("view all " + photo.commentCount + " comments");
        tvComment1.setText(Html.fromHtml(photo.comment1));
        tvComment2.setText(Html.fromHtml(photo.comment2));

        long createdTime = Long.parseLong(photo.createdTime);
        CharSequence reletiveTime = DateUtils.getRelativeTimeSpanString(createdTime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        tvTime.setText(reletiveTime);


        // clear out the imageview if it was recycled right away
        ivPhoto.setImageResource(0);
        // insert image using picasso (send out async)
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.mipmap.ic_launcher).into(ivPhoto);

        ivProfile.setImageResource(0);
        // insert image using picasso (send out async)
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.with(getContext()).load(photo.profilePicUrl).placeholder(R.mipmap.ic_launcher).transform(transformation).into(ivProfile);



        // return the created item as a view
        return convertView;
    }
}
