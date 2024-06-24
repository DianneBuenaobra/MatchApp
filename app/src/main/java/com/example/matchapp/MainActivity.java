package com.example.matchapp;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ImageView[] items = new ImageView[12];
    int[] images = new int[12];
    Integer[] cards = {0, 1, 2, 3, 4, 5, 0, 1, 2, 3, 4, 5};
    boolean[] cardMatched = new boolean[12];
    int firstCard, secondCard, firstClicked, secondClicked;
    int cardNum = 1;
    int matchedPairs = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the ImageViews
        items[0] = findViewById(R.id.item1);
        items[1] = findViewById(R.id.item2);
        items[2] = findViewById(R.id.item3);
        items[3] = findViewById(R.id.item4);
        items[4] = findViewById(R.id.item5);
        items[5] = findViewById(R.id.item6);
        items[6] = findViewById(R.id.item7);
        items[7] = findViewById(R.id.item8);
        items[8] = findViewById(R.id.item9);
        items[9] = findViewById(R.id.item10);
        items[10] = findViewById(R.id.item11);
        items[11] = findViewById(R.id.item12);

        // Set tags to identify ImageView index
        for (int i = 0; i < items.length; i++) {
            items[i].setTag(String.valueOf(i));
            items[i].setOnClickListener(this::onCardClick);
        }

        frontCardPictures();
        Collections.shuffle(Arrays.asList(cards));
    }

    private void frontCardPictures() {
        images[0] = R.drawable.img1;
        images[1] = R.drawable.img2;
        images[2] = R.drawable.img3;
        images[3] = R.drawable.img4;
        images[4] = R.drawable.img5;
        images[5] = R.drawable.img6;
        images[6] = R.drawable.img1;
        images[7] = R.drawable.img2;
        images[8] = R.drawable.img3;
        images[9] = R.drawable.img4;
        images[10] = R.drawable.img5;
        images[11] = R.drawable.img6;
    }

    private void onCardClick(View view) {
        int cardIndex = Integer.parseInt((String) view.getTag());
        flipCard((ImageView) view, cardIndex);

        if (cardNum == 1) {
            firstCard = cards[cardIndex];
            firstClicked = cardIndex;
            cardNum = 2;
            items[cardIndex].setEnabled(false);
        } else if (cardNum == 2) {
            secondCard = cards[cardIndex];
            secondClicked = cardIndex;
            cardNum = 1;

            disableAllCards();

            new Handler().postDelayed(() -> {
                checkMatch();
                enableAllCards();
                checkEnd();
            }, 1000);
        }
    }

    private void flipCard(ImageView imageView, int cardIndex) {
        AnimatorSet setOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_out);
        AnimatorSet setIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_in);

        setOut.setTarget(imageView);
        setOut.start();

        setOut.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);
                imageView.setImageResource(images[cards[cardIndex]]);
                setIn.setTarget(imageView);
                setIn.start();
            }
        });
    }

    private void checkMatch() {
        if (firstCard == secondCard) {
            items[firstClicked].setEnabled(false);
            items[secondClicked].setEnabled(false);
            cardMatched[firstClicked] = true;
            cardMatched[secondClicked] = true;
            matchedPairs++;
        } else {
            items[firstClicked].setImageResource(R.drawable.questionmarkcard);
            items[secondClicked].setImageResource(R.drawable.questionmarkcard);
        }
    }

    private void disableAllCards() {
        for (ImageView item : items) {
            item.setEnabled(false);
        }
    }

    private void enableAllCards() {
        for (int i = 0; i < items.length; i++) {
            if (!cardMatched[i]) {
                items[i].setEnabled(true);
            }
        }
    }

    private void checkEnd() {
        if (matchedPairs == images.length / 2) {
            Toast.makeText(this, "Congratulations! You've matched all pairs!", Toast.LENGTH_LONG).show();
        }
    }
}
