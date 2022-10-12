package com.example.twenty_one;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Activity2 extends AppCompatActivity {

    //Declare variables
    private final String[] cardValues = {"", "a", "2", "3", "4", "5", "6", "7", "8", "9", "10", "j", "q", "k"};
    private final String[] cardSuit = {"", "d", "s", "h", "c"};
    private final int BUST_MAX = 23;
    private final int BUST_MIN = -23;
    private final int D_MIN = 17;
    ArrayList<String> userCards = new ArrayList<String>();
    ArrayList<String> dealerCards = new ArrayList<String>();
    ArrayList<String> usedCards = new ArrayList<String>();
    ArrayList<String> discarded = new ArrayList<String>();
    int counter = 0;
    TextView pointTextView;
    TextView pointTextView2;
    int userPoints = 0;
    int dealerPoints = 0;
    Boolean u_bust = false;
    Boolean d_bust = false;
    Boolean uCheck = false, dCheck = false;
    Boolean setCard = false;
    int imageIdCount = 0;
    int discard = 0;
    int u_total_wins = 0;
    int d_total_wins = 0;
    AlertDialog.Builder builder;
    Button hitButton;
    Button stayButton;
    Boolean imageIdSet = false;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("userCardsList", userCards);
        savedInstanceState.putStringArrayList("discardedList", discarded);
        savedInstanceState.putStringArrayList("dealerCardsList", dealerCards);
        savedInstanceState.putStringArrayList("usedCardsList", usedCards);
        savedInstanceState.putInt("userPointsSave", userPoints);
        savedInstanceState.putInt("dealerPointsSave", dealerPoints);
        savedInstanceState.putBoolean("u_bustSave", u_bust);
        savedInstanceState.putBoolean("d_bustSave", d_bust);
        savedInstanceState.putBoolean("setCardBool", setCard);
        savedInstanceState.putBoolean("uCheckSave", uCheck);
        savedInstanceState.putBoolean("dCheckSave", dCheck);
        savedInstanceState.putInt("discardSave", discard);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        if (savedInstanceState != null){
            discarded = savedInstanceState.getStringArrayList("discardedList");
            userCards = savedInstanceState.getStringArrayList("userCardsList");
            dealerCards = savedInstanceState.getStringArrayList("dealerCardsList");
            usedCards = savedInstanceState.getStringArrayList("usedCardsList");
            userPoints = savedInstanceState.getInt("userPointsSave");
            dealerPoints = savedInstanceState.getInt("dealerPointsSave");
            u_bust = savedInstanceState.getBoolean("u_bustSave");
            d_bust = savedInstanceState.getBoolean("d_bustSave");
            setCard = savedInstanceState.getBoolean("setCardBool");
            uCheck = savedInstanceState.getBoolean("uCheckSave");
            dCheck = savedInstanceState.getBoolean("dCheckSave");
            discard = savedInstanceState.getInt("discardSave");
        }


        pointTextView2 = findViewById(R.id.dealer_points);
        pointTextView2.setVisibility(View.INVISIBLE);
        hitButton = findViewById(R.id.hit_btn);
        stayButton = findViewById(R.id.stay_btn);
        builder = new AlertDialog.Builder(this);

        // deal first two cards
        dealFirstCards();

        if (userCards.get(0).contains("2") && userCards.get(1).contains("3")) {
            uCheck = true;
        }
        if (dealerCards.get(0).contains("2") && dealerCards.get(1).contains("3")) {
            dCheck = true;
        }

        if (uCheck || dCheck) {
            // end game
            hitButton.setEnabled(false);
            stayButton.setEnabled(false);
            if (!setCard) {
                setDealerCard1();
            }
            pointTextView = findViewById(R.id.dealer_points);
            pointTextView.setVisibility(View.VISIBLE);
            hitButton.postDelayed(afterDelay, 2000);
        }

        Button mainM = findViewById(R.id.mm_btn);
        mainM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity2.this, MainActivity.class);
                startActivity(i);
            }
        });


        Button exit = findViewById(R.id.exit_a2_btn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

        // hit

        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitButton.postDelayed(afterHit, 500);
            }
        });



        // stay
        stayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                pointTextView2.setVisibility(View.VISIBLE);

                // display computer's first card
                setDealerCard1();
                Boolean notbusted = true;
                do {
                    //Get next card
                    notbusted = checkPoints(dealerPoints);
                    if (!notbusted && dealerCards.size() > 2) {
                        d_bust = true;
                        break;
                    }
                    if(notbusted) {
                        if (dealerPoints >= D_MIN || dealerPoints <= -17) {
                            break;
                        }
                    }

                    getNextCard(dealerCards, false);
                    String card;
                    counter = dealerCards.size() - 1;
                    card = dealerCards.get(counter);
                    getCardImage(card, false);
                } while (dealerCards.size() < 10);

                stayButton.postDelayed(afterDelay, 2000);
            }
        });
    }

    private void dealFirstCards(){
        if(userCards.isEmpty() && dealerCards.isEmpty()) {
            getNextCard(userCards, true);
            getNextCard(dealerCards, false);
            getNextCard(userCards, true);
            getNextCard(dealerCards, false);

            discarded.add("nd");
            discarded.add("nd");
        } else{imageIdSet = true;}

        for(int xx = 0; xx < userCards.size(); xx++){
           getCardImage(userCards.get(xx), true);
        }
        getCardImage(dealerCards.get(1), false);
    }

    private void setDealerCard1(){
        setCard = true;
        Resources res = getResources();
        int resID = res.getIdentifier(dealerCards.get(0), "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID);
        ImageView dImg1 = findViewById(R.id.dealer_card1);
        dImg1.setImageDrawable(drawable);
    }

    private Runnable afterDelay = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(Activity2.this, end_screen_activity.class);
            int user_abs = Math.abs(userPoints);
            int dealer_abs = Math.abs(dealerPoints);

            if(!setCard){setDealerCard1();}

            if(user_abs > BUST_MAX){u_bust = true;}
            if(dealer_abs > BUST_MAX){d_bust = true;}

            if(uCheck && dCheck){i.putExtra("key", "It's a tie."); }
            else if(dCheck){i.putExtra("key", "You lose."); d_total_wins = 1;}
            else if(uCheck){i.putExtra("key", "You Win!");u_total_wins = 1;}
            else {
                if (!u_bust) {
                    if (!d_bust) {
                        if (user_abs > dealer_abs) {
                            i.putExtra("key", "You Win!"); u_total_wins = 1;
                        } else if (user_abs == dealer_abs) {
                            i.putExtra("key", "It's a tie.");
                        } else {
                            i.putExtra("key", "You lose."); d_total_wins = 1;
                        }
                    } else {
                        i.putExtra("key", "You Win!"); u_total_wins = 1;
                    }
                } else {
                    if (!d_bust) {
                        i.putExtra("key", "You lose."); d_total_wins = 1;
                    } else {
                        i.putExtra("key", "You both busted");
                    }
                }
            }

            i.putExtra("key2", userPoints);
            i.putExtra("key3", dealerPoints);
            i.putExtra("key4", u_total_wins);
            i.putExtra("key5", d_total_wins);
            startActivity(i);
        }
    };


    public Boolean checkPoints(int points){
        return points <= BUST_MAX && points >= BUST_MIN;
    }

    public void getNextCard(ArrayList<String> cardArray, boolean user){
        Boolean cardTaken = false;
        int randCardV;
        int randCardS;
        String hold = "";

        do {
            cardTaken = false;
            randCardV = (int)Math.floor(Math.random()*(13-1+1)+1);
            randCardS = (int)Math.floor(Math.random()*(4-1+1)+1);
            hold = cardSuit[randCardS] + cardValues[randCardV];
            if(usedCards.contains(hold)) {
                cardTaken = true;
            }
        }  while(cardTaken);
        cardArray.add(hold);
        usedCards.add(hold);


        if(user) {
            if (randCardS == 2 || randCardS == 4) {
                userPoints = randCardV;
            } else {
                userPoints = (-1) * randCardV;
            }
            pointTextView = findViewById(R.id.user_points);
            String temp = pointTextView.getText().toString();
            int tempI = Integer.parseInt(temp);
            userPoints += tempI;
            pointTextView.setText("" + userPoints);
        }
        else {
            if (randCardS == 2 || randCardS == 4) {
                dealerPoints = randCardV;
            } else {
                dealerPoints = (-1) * randCardV;
            }
            String temp = pointTextView2.getText().toString();
            int tempI = Integer.parseInt(temp);
            dealerPoints += tempI;
            pointTextView2.setText("" + dealerPoints);
        }
    }

    public void getCardImage(String mDrawName, Boolean user){
        Resources res = getResources();
        int resID = res.getIdentifier(mDrawName , "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID);
        ImageView image = new ImageView(this);
        image.setImageDrawable(drawable);
        image.setPadding(10, 10, 10, 10);

        TableRow tbr;
        if (user) {
            image.setId(imageIdCount);
            image.setOnClickListener(imageSelect);
            tbr = findViewById(R.id.u_row1);
        }
        else {
            if (dealerCards.size() > 7) {
                tbr = findViewById(R.id.dealer_row2);
            } else {
                tbr = findViewById(R.id.dealer_row1);
            }
        }
        if(user) {
            if (discarded.get(imageIdCount).equals("nd")) {
                tbr.addView(image);
            }
            imageIdCount++;
        }
        else{
            tbr.addView(image);
        }
    }

    private View.OnClickListener imageSelect = new View.OnClickListener() {
        public void onClick(View v) {
            //ask user if they want to discard
            if((checkPoints(userPoints) || userCards.size() == 2) && discard < 3){
                builder.setMessage("Do you want to discard this card?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                discard++;
                                int vid = v.getId();
                                String cardBeingRemoved = userCards.get(vid);
                                discarded.set(vid, "d");

                                //remove view
                                v.setVisibility(View.GONE);

                                //fix points
                                if (cardBeingRemoved.length() >= 2) {
                                    String t = cardBeingRemoved.substring(1);
                                    int p = 0;
                                    if (t.charAt(0) >= '0' && t.charAt(0) <= '9') {
                                        p = Integer.parseInt(t);
                                        p = -p;
                                    } else if (t.equals("a")) {
                                        p = -1;
                                    } else if (t.equals("j")) {
                                        p = -11;
                                    } else if (t.equals("q")) {
                                        p = -12;
                                    } else if (t.equals("k")) {
                                        p = -13;
                                    }


                                    if(cardBeingRemoved.contains("s") || cardBeingRemoved.contains("c")){
                                        int absP = p;
                                        p = Math.abs(absP);
                                    }
                                    userPoints -= p;
                                    pointTextView = findViewById(R.id.user_points);
                                    pointTextView.setText(Integer.toString(userPoints));
                                    hitButton.postDelayed(afterHit, 500);
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.setTitle("Discard");
                alert.show();
            }
        }
    };

    private Runnable afterHit = new Runnable() {
        @Override
        public void run() {
            if (!checkPoints(userPoints) && userCards.size() > 2) {
                u_bust = true;
                Toast.makeText(getApplicationContext(), "You have busted.", Toast.LENGTH_SHORT).show();
            } else {
                int numOfCards = userCards.size() - discard;
                if (numOfCards < 7) {
                    imageIdSet = false;
                    getNextCard(userCards, true);
                    String card;
                    counter = userCards.size() - 1;
                    card = userCards.get(counter);
                    discarded.add("nd");
                    getCardImage(card, true);
                } else {
                    if (discard > 2) {
                        Toast.makeText(getApplicationContext(), "You turn is over.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "You can discard and redraw or stay.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

}