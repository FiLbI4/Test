package com.mdd.aleksandrfilcenko.pedometer;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import de.hdodenhof.circleimageview.CircleImageView;

class TopCardProvider extends CardProvider<TopCardProvider> {

    private String avatar;
    private String crown;
    private String name;
    private String steps;
    private int numberTop;

    TopCardProvider setAvatar(String avatar){
        this.avatar = avatar;
        notifyDataSetChanged();
        return this;
    }


    TopCardProvider setCrown(String crown){
        this.crown = crown;
        notifyDataSetChanged();
        return this;
    }


    TopCardProvider setSteps(String steps){
        this.steps = steps;
        notifyDataSetChanged();
        return this;
    }

    public TopCardProvider setName(String name){
        this.name = name;
        notifyDataSetChanged();
        return this;
    }

    TopCardProvider setNumberTop(int numberTop){
        this.numberTop = numberTop;
        notifyDataSetChanged();
        return this;
    }



    public String getImageUrl() {
        return avatar;
    }

    private String getCrown() {
        return crown;
    }

    public String getName() {
        return name;
    }


    @Override
    public void render(@NonNull View view, @NonNull Card card) {
        super.render(view, card);

        TextView textName = (TextView)view.findViewById(R.id.name);
        textName.setText(name);

        TextView textSteps = (TextView)view.findViewById(R.id.steps);
        textSteps.setText(String.valueOf(steps));

        TextView textNumberTop = (TextView)view.findViewById(R.id.number_top);
        textNumberTop.setText(String.valueOf(numberTop));


        final CircleImageView imageView = findViewById(view, R.id.avatar, CircleImageView.class);
        if (imageView != null) {
            if (getDrawable() != null) {
                imageView.setImageDrawable(getDrawable());
            } else {
                final RequestCreator requestCreator = Picasso.with(getContext())
                        .load(getImageUrl());

                if (getOnImageConfigListenerListener() != null) {
                    getOnImageConfigListenerListener().onImageConfigure(requestCreator);
                }
                requestCreator.into(imageView);
            }
        }

        final ImageView crownImageView = findViewById(view, R.id.crown_top, ImageView.class);
        if (crownImageView != null) {
            if (getDrawable() != null) {
                crownImageView.setImageDrawable(getDrawable());
            } else {
                final RequestCreator requestCreator = Picasso.with(getContext())
                        .load(getCrown());

                if (getOnImageConfigListenerListener() != null) {
                    getOnImageConfigListenerListener().onImageConfigure(requestCreator);
                }
                requestCreator.into(crownImageView);
            }
        }
    }


}
