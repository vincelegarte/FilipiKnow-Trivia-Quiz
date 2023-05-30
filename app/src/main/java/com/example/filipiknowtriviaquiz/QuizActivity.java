package com.example.filipiknowtriviaquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class QuizActivity extends AppCompatActivity {

    private TextView text_stage;
    private TextView text_number;
    private TextView text_timer;
    private TextView text_question;
    private GifImageView image_life1;
    private GifImageView image_life2;
    private GifImageView image_life3;
    private RadioGroup radio_choices;
    private RadioButton button_choice1;
    private RadioButton button_choice2;
    private RadioButton button_choice3;
    private RadioButton button_choice4;
    private Button button_next;

    private static final long EASY_COUNTDOWN_IN_MILLIS = 20000;
    private static final long MEDIUM_COUNTDOWN_IN_MILLIS = 15000;
    private static final long HARD_COUNTDOWN_IN_MILLIS = 10000;

    private CountDownTimer countDownTimer;
    private Long timeLeftInMillis;

    private List<QuestionModel> questionsEasy1;
    private List<QuestionModel> questionsEasy2;
    private List<QuestionModel> questionsMedium1;
    private List<QuestionModel> questionsMedium2;
    private List<QuestionModel> questionsHard1;
    private List<QuestionModel> questionsHard2;
    private List<String> choices;

    int itemsEasy1;
    int itemsEasy2;
    int itemsMedium1;
    int itemsMedium2;
    int itemsHard1;
    int itemsHard2;

    int numberEasy1 = 0;
    int numberEasy2 = 0;
    int numberMedium1 = 0;
    int numberMedium2 = 0;
    int numberHard1 = 0;
    int numberHard2 = 0;

    int life = 3;

    int score = 0;

    private QuestionModel currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionsEasy1 = new ArrayList<>();
        questionsEasy2 = new ArrayList<>();
        questionsMedium1 = new ArrayList<>();
        questionsMedium2 = new ArrayList<>();
        questionsHard1 = new ArrayList<>();
        questionsHard2 = new ArrayList<>();
        choices = new ArrayList<>();

        text_stage = findViewById(R.id.text_stage);
        text_number = findViewById(R.id.text_number);
        image_life1 = findViewById(R.id.image_life1);
        image_life2 = findViewById(R.id.image_life2);
        image_life3 = findViewById(R.id.image_life3);
        text_timer = findViewById(R.id.text_timer);
        text_question = findViewById(R.id.text_question);
        radio_choices = findViewById(R.id.radio_choices);
        button_choice1 = findViewById(R.id.button_choice1);
        button_choice2 = findViewById(R.id.button_choice2);
        button_choice3 = findViewById(R.id.button_choice3);
        button_choice4 = findViewById(R.id.button_choice4);
        button_next = findViewById(R.id.button_next);

        questionsList();

        itemsEasy1 = questionsEasy1.size();
        itemsEasy2 = questionsEasy2.size();
        itemsMedium1 = questionsMedium1.size();
        itemsMedium2 = questionsMedium2.size();
        itemsHard1 = questionsHard1.size();
        itemsHard2 = questionsHard2.size();

        Collections.shuffle(questionsEasy1);
        Collections.shuffle(questionsEasy2);
        Collections.shuffle(questionsMedium1);
        Collections.shuffle(questionsMedium2);
        Collections.shuffle(questionsHard1);
        Collections.shuffle(questionsHard2);

        showNextQuestion();

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((button_choice1.isChecked() || button_choice2.isChecked() || button_choice3.isChecked() || button_choice4.isChecked()) && life != 0){
                    checkAnswer();
                } else {
                    Toast.makeText(QuizActivity.this, "Please select your answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkAnswer() {

        countDownTimer.cancel();

        RadioButton selected = findViewById(radio_choices.getCheckedRadioButtonId());
        if (selected == null || !currentQuestion.getKeyAnswer().equals(selected.getText().toString())){
            life-=1;
        } else {
            score+=1;
        }

        if (life == 2){
            image_life3.setVisibility(View.GONE);
        } else if (life == 1){
            image_life2.setVisibility(View.GONE);
        } else if (life == 0){
            image_life1.setVisibility(View.GONE);
            Intent intent = new Intent(QuizActivity.this, GameOverActivity.class);
            startActivity(intent);
            super.onBackPressed();
        }

        if (numberHard2 == itemsHard2 && life != 0){
            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
            intent.putExtra("score",Integer.toString(score));
            startActivity(intent);
            super.onBackPressed();
        }

        if (numberEasy1 < itemsEasy1 && life != 0){
            showNextQuestion();
        } else if (numberEasy2 < itemsEasy2 && life != 0){
            showNextQuestion();
        } else if (numberMedium1 < itemsMedium1 && life != 0){
            showNextQuestion();
        } else if (numberMedium2 < itemsMedium2 && life != 0){
            showNextQuestion();
        } else if (numberHard1 < itemsHard1 && life != 0){
            showNextQuestion();
        } else if (numberHard2 < itemsHard2 && life != 0){
            showNextQuestion();
        }
    }

    private void showNextQuestion() {

        radio_choices.clearCheck();
        choices.clear();

        if (numberEasy1 < itemsEasy1){
            text_stage.setText("Easy - Level 1");
            currentQuestion = questionsEasy1.get(numberEasy1);
            text_question.setText(currentQuestion.getQuestion());

            choices.add(currentQuestion.getChoice1());
            choices.add(currentQuestion.getChoice2());
            choices.add(currentQuestion.getChoice3());
            choices.add(currentQuestion.getChoice4());
            Collections.shuffle(choices);

            button_choice1.setText(choices.get(0));
            button_choice2.setText(choices.get(1));
            button_choice3.setText(choices.get(2));
            button_choice4.setText(choices.get(3));

            numberEasy1++;
            text_number.setText("Question: "+ numberEasy1 +"/"+ itemsEasy1);

            timeLeftInMillis = EASY_COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else if (numberEasy2 < itemsEasy2){
            text_stage.setText("Easy - Level 2");
            currentQuestion = questionsEasy2.get(numberEasy2);
            text_question.setText(currentQuestion.getQuestion());

            choices.add(currentQuestion.getChoice1());
            choices.add(currentQuestion.getChoice2());
            choices.add(currentQuestion.getChoice3());
            choices.add(currentQuestion.getChoice4());
            Collections.shuffle(choices);

            button_choice1.setText(choices.get(0));
            button_choice2.setText(choices.get(1));
            button_choice3.setText(choices.get(2));
            button_choice4.setText(choices.get(3));

            numberEasy2++;
            text_number.setText("Question: "+ numberEasy2 +"/"+ itemsEasy2);

            timeLeftInMillis = EASY_COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else if (numberMedium1 < itemsMedium1){
            text_stage.setText("Medium - Level 1");
            currentQuestion = questionsMedium1.get(numberMedium1);
            text_question.setText(currentQuestion.getQuestion());

            choices.add(currentQuestion.getChoice1());
            choices.add(currentQuestion.getChoice2());
            choices.add(currentQuestion.getChoice3());
            choices.add(currentQuestion.getChoice4());
            Collections.shuffle(choices);

            button_choice1.setText(choices.get(0));
            button_choice2.setText(choices.get(1));
            button_choice3.setText(choices.get(2));
            button_choice4.setText(choices.get(3));

            numberMedium1++;
            text_number.setText("Question: "+ numberMedium1 +"/"+ itemsMedium1);

            timeLeftInMillis = MEDIUM_COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else if (numberMedium2 < itemsMedium2){
            text_stage.setText("Medium - Level 2");
            currentQuestion = questionsMedium2.get(numberMedium2);
            text_question.setText(currentQuestion.getQuestion());

            choices.add(currentQuestion.getChoice1());
            choices.add(currentQuestion.getChoice2());
            choices.add(currentQuestion.getChoice3());
            choices.add(currentQuestion.getChoice4());
            Collections.shuffle(choices);

            button_choice1.setText(choices.get(0));
            button_choice2.setText(choices.get(1));
            button_choice3.setText(choices.get(2));
            button_choice4.setText(choices.get(3));

            numberMedium2++;
            text_number.setText("Question: "+ numberMedium2 +"/"+ itemsMedium2);

            timeLeftInMillis = MEDIUM_COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else if (numberHard1 < itemsHard1){
            text_stage.setText("Hard - Level 1");
            currentQuestion = questionsHard1.get(numberHard1);
            text_question.setText(currentQuestion.getQuestion());

            choices.add(currentQuestion.getChoice1());
            choices.add(currentQuestion.getChoice2());
            choices.add(currentQuestion.getChoice3());
            choices.add(currentQuestion.getChoice4());
            Collections.shuffle(choices);

            button_choice1.setText(choices.get(0));
            button_choice2.setText(choices.get(1));
            button_choice3.setText(choices.get(2));
            button_choice4.setText(choices.get(3));

            numberHard1++;
            text_number.setText("Question: "+ numberHard1 +"/"+ itemsHard1);

            timeLeftInMillis = HARD_COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else if (numberHard2 < itemsHard2){
            text_stage.setText("Hard - Level 2");
            currentQuestion = questionsHard2.get(numberHard2);
            text_question.setText(currentQuestion.getQuestion());

            choices.add(currentQuestion.getChoice1());
            choices.add(currentQuestion.getChoice2());
            choices.add(currentQuestion.getChoice3());
            choices.add(currentQuestion.getChoice4());
            Collections.shuffle(choices);

            button_choice1.setText(choices.get(0));
            button_choice2.setText(choices.get(1));
            button_choice3.setText(choices.get(2));
            button_choice4.setText(choices.get(3));

            numberHard2++;
            text_number.setText("Question: "+ numberHard2 +"/"+ itemsHard2);

            timeLeftInMillis = HARD_COUNTDOWN_IN_MILLIS;
            startCountDown();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis = l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = Long.valueOf(0);
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        text_timer.setText(Integer.toString(seconds));
    }

    private void questionsList() {

        questionsEasy1.add(new QuestionModel("Sino ang pambansang bayani?", "Apolinario Mabini", "Emilio Aguinaldo", "Dr. Jose Rizal", "Andres Bonifacio", "Dr. Jose Rizal"));
        questionsEasy1.add(new QuestionModel("Ano ang pambansang kasuotan ng babae?", "Baro at saya", "Barong tagalog", "Bestida", "Kamiseta", "Baro at saya"));
        questionsEasy1.add(new QuestionModel("Ano ang pambansang hayop?", "Baka", "Kalabaw", "Agila", "Maya", "Kalabaw"));
        questionsEasy1.add(new QuestionModel("Ano ang pambansang puno?", "Mahogany", "Acacia", "Pine Tree", "Narra", "Narra"));
        questionsEasy1.add(new QuestionModel("Sino ang ina ng Katipunan o mas kilala bilang Tandang Sora?", "Imelda Marcos", "Melchora Aquino", "Corazon Aquino", "Josephine Bracken", "Melchora Aquino"));
        questionsEasy1.add(new QuestionModel("Sino ang utak ng himagsikan?", "Apolinario Mabini", "Emilio Aguinaldo", "Dr. Jose Rizal", "Andres Bonifacio", "Apolinario Mabini"));
        questionsEasy1.add(new QuestionModel("Ano ang pinakamalaking anyong tubig", "Ilog", "Dagat", "Karagatan", "Sapa", "Karagatan"));
        questionsEasy1.add(new QuestionModel("Ano ang pambansang laro ng Pilipinas?", "Sepak Takraw", "Arnis", "Mobile Legends", "Basketball", "Arnis"));
        questionsEasy1.add(new QuestionModel("Ano ang pambansang dahon ng Pilipinas?", "Malunggay", "Alugbati", "Atis", "Anahaw", "Anahaw"));
        questionsEasy1.add(new QuestionModel("Kailan namatay si Dr. Jose Rizal?", "December 30, 1984", "December 30, 1985", "December 30, 1986", "December 30, 1987","December 30, 1986"));

        questionsEasy2.add(new QuestionModel("Saan sa Pilipinas ang tinaguriang pinakamatandang lungsod?", "Cebu", "Palawan", "Vigan", "Bohol", "Cebu"));
        questionsEasy2.add(new QuestionModel("Ilan ang termino ng Pangulo ng Pilipinas?", "Apat na taon", "Limang taon ", "Anim na taon", "Pitong taon", "Anim na taon"));
        questionsEasy2.add(new QuestionModel("Aling bansa ang may kolonya ng Pilipinas sa loob ng mahigit 300 taon?", "Espanya", "Italy", "France", "England", "Espanya"));
        questionsEasy2.add(new QuestionModel("Alin sa mga sumusunod na pangkat ang kasapi ng Pilipinas?", "NATO", "ASEAN", "SAARC", "OAU", "ASEAN"));
        questionsEasy2.add(new QuestionModel("Sino ang nagsabi ng katagang \"A Filipino is worth dying for\"?", "Noynoy Aquino", "Ferdinand Marcos", "Ninoy Aquino", "Manuel L. Quezon", "Ninoy Aquino"));
        questionsEasy2.add(new QuestionModel("Ano ang pambansang bulaklak ng Pilipinas?", "Sampaguita", "Orchid", "Rose", "Bauhinia", "Sampaguita"));
        questionsEasy2.add(new QuestionModel("Anong siyudad ang kilala bilang \"Summer Capital of the Philippines\"?", "Tagaytay City", "Baguio City", "Cebu City", "Davao City", "Baguio City"));
        questionsEasy2.add(new QuestionModel("Saang kontinente nabibilang ang Pilipinas?", "Europe", "South America", "North America", "Asia", "Asia"));
        questionsEasy2.add(new QuestionModel("Kailan ipinagdiriwang ang Philippines Independence Day?", "June 12", "December 25", "July 11", "August 4", "June 12"));
        questionsEasy2.add(new QuestionModel("Saan matatagpuan ang Mayon Volcano?", "Batanes", "Albay", "Zambales", "Cagayan", "Albay"));

        questionsMedium1.add(new QuestionModel("Tawag sa lugar na may higit sa isang daang pamilya?", "Barangay", "Poblacion", "Balangay", "Bayan", "Barangay"));
        questionsMedium1.add(new QuestionModel("Sinaunang bangkang Malay na dumaong lulan ang higit sa isang daang pamilya?", "Banalay", "Banagay", "Barangay", "Balangay", "Balangay"));
        questionsMedium1.add(new QuestionModel("Saan lugar natuklasan ang pinakamatandang dokumento ng Pilipinas?", "Tondo", "Maynila", "Laguna", "Bataan", "Laguna"));
        questionsMedium1.add(new QuestionModel("Anong bansa ang sumakop sa Pilipinas noong Ikalawang Digmaang Pandaigdig?", "Germany", "China", "Italy", "Japan", "Japan"));
        questionsMedium1.add(new QuestionModel("Alin ang nangingibabaw na relihiyon sa Pilipinas?", "Islam", "Budismo", "Hudaismo", "Kristiyanismo", "Kristiyanismo"));
        questionsMedium1.add(new QuestionModel("Sino ang bayani ng Labanan sa Tirad Pass?", "Antonio Moon", "Diego Silang", "Gregrio Del Pillar", "Paa ng Macario", "Gregrio Del Pillar"));
        questionsMedium1.add(new QuestionModel("Ano ang unang aklat na nailathala sa Pilipinas?", "Doktrina ng Kristiyano", "Pasiong Mahal", "Noli Me Tangere", "Barlaam at Josaphat", "Doktrina ng Kristiyano"));
        questionsMedium1.add(new QuestionModel("Anong lugar sa Pilipinas ang kilala rin bilang \"walled city\"?", "Fort Santiago", "Plaza Miranda", "Intramuros", "Luneta", "Intramuros"));
        questionsMedium1.add(new QuestionModel("Sino ang ikatlo at huling gobernador militar ng Pilipinas?", "Heneral Elwell Otis", "Heneral Arthur MacArthur", "Heneral Wesley Merrit", "Heneral Douglas MacArthur", "Heneral Arthur MacArthur"));
        questionsMedium1.add(new QuestionModel("Alin sa mga sumusunod na wika ang katutubo sa Pilipinas?", "Basque", "Catalan", "Galician", "Tagalog", "Tagalog"));

        questionsMedium2.add(new QuestionModel("Ano ang mga probinsya na may akronim na CALABARZON?", "Cagayan, Landusan, Baguio, Romblon, Quinaguitman", "Cavite, Laguna, Batangas, Rizal, Quezon", "Capiz, Langob, Bohol, Rosario, Quirhat", "Wala ang sagot sa lahat ng letra", "Cavite, Laguna, Batangas, Rizal, Quezon"));
        questionsMedium2.add(new QuestionModel("Saan sa Maynila matatagpuan ang San Agustin Church?", "Intramuros", "Metro Manila", "Batangas", "Calamba", "Intramuros"));
        questionsMedium2.add(new QuestionModel("Ang \"Pahiyas\" ay isang pagdiriwang na ipinagdiriwang tuwing Mayo sa anong bayan sa lalawigan ng Quezon?", "Lucban", "Laoag", "Palawan", "Zamboanga", "Lucban"));
        questionsMedium2.add(new QuestionModel("Ano ang tanyag na tawag sa tirahan ng Pangulo ng Pilipinas?", "Presidential Palace", "Malacanang Palace", "White House", "Buckingham Palace", "Malacanang Palace"));
        questionsMedium2.add(new QuestionModel("Alin ang karaniwang pagkaing kalye sa Pilipinas?", "Balut", "Takoyaki", "Croquetas", "Pirozhki","Balut"));
        questionsMedium2.add(new QuestionModel("Ano ang pangalan ng bulkan na matatagpuan sa maliit na isla ng Camiguin?", "Mt. Ranges", "Mt. Tibok-Tibok", "Mt. Hibok-Hibok", "Mt. Spratlys", "Mt. Hibok-Hibok"));
        questionsMedium2.add(new QuestionModel("Sinong bayani ng Pilipinas ang ipinanganak sa Ilocos Norte at nakilala rin sa kanyang \"Spoliarium\"?", "Gen. Antonio Luna", "Andres Bonifacio", "Emilio Jacinto", "Juan Luna", "Juan Luna"));
        questionsMedium2.add(new QuestionModel("Sino ang itinuturing na pangalawang pangulo ng Pilipinas?", "Fidel Castro","Manuel L. Quezon", "Fidel V. Ramos", "Ramon Magsaysay", "Manuel L. Quezon"));
        questionsMedium2.add(new QuestionModel("Ano ang opisyal na pangalan ng Pilipinas?", "State of the Philippines", "Republic of the Philippines", "People\'s Republic of the Philippines", "Philippines Federation","Republic of the Philippines"));
        questionsMedium2.add(new QuestionModel("Sino ang unang babaeng Presidente ng Pilipinas?", "Gloria Macapagal Arroyo", "Vigdis Finnbogadottir", "Yevgenia Bosch", "Maria Corazon Cojuangco Aquino", "Maria Corazon Cojuangco Aquino"));

        questionsHard1.add(new QuestionModel("Sa Pilipinas, Aling lungsod ang kilala bilang \"City of Friendship\"?", "Davao City", "Tagbilaran City", "Marikina City", "Cagayan De Oro City", "Tagbilaran City"));
        questionsHard1.add(new QuestionModel("Ano ang pinakamahabang ilog sa Pilipinas?", "Cagayan River", "Mindanao River", "Agusan River", "Pasig River", "Cagayan River"));
        questionsHard1.add(new QuestionModel("Sa anong taon nakuha ng Pilipinas ang kalayaan mula sa USA?", "1989", "1971", "1946", "1979", "1946"));
        questionsHard1.add(new QuestionModel("Sino ang nagmungkahi ng ideya ng Philippine Autonomy Act?", "Manuel L. Quezon", "Manuel Roxas", "Sergio Osmena", "William Atkinson Jones", "William Atkinson Jones"));
        questionsHard1.add(new QuestionModel("Sino sa mga sumusunod na dating pangulo ang namatay sa pagbagsak ng eroplano?", "Ferdinand Marcos", "Ramon Magsaysay", "Carlos Garcia","Emilio Aguinaldo", "Ramon Magsaysay"));
        questionsHard1.add(new QuestionModel("Sino ang nagpangalan sa bansang \"St. Lazarus Islands\"?", "Alvaro Saavedra", "Michael Lopez de Legazpi", "Ferdinand Magellan", "Diego Barbosa", "Ferdinand Magellan"));
        questionsHard1.add(new QuestionModel("Sino ang lumaban sa pamahalaang Espanyol dahil hindi nagbigay ng pormal na seremonya ng libing ang pamahalaan para sa kanyang kapatid?", "Francisco Dagohoy", "Diego Silang", "Macario Sacay", "Andres Bonifacio", "Francisco Dagohoy"));
        questionsHard1.add(new QuestionModel("Sa panahon ng pananakop ng mga Hapones sa Pilipinas, isang pangulong Pilipino ang hinirang ng mga Hapones. Sino siya?", "Ramon Magsaysay", "Jose P. Laurel Sr.", "Sergio Osmena", "Josefino Cotas Sr.", "Jose P. Laurel Sr."));
        questionsHard1.add(new QuestionModel("Kailan nagsimula ang orihinal na Edsa (People's Power) Revolution?", "Pebrero 22, 1986", "Pebrero 26, 2002", "Enero 19, 2002", "Enero 19, 1986", "Pebrero 22, 1986"));
        questionsHard1.add(new QuestionModel("Anong kilos ang kilala rin sa tawag na \"Batas Militar\"?", "Proclamation 1081", "Kautusang Tagapagpaganap blg. 25 S. 1974", "Proclamation 274", "Article 6 Section 14 of the 1973 Constitution", "Proclamation 1081"));

        questionsHard2.add(new QuestionModel("Anong bagay ang natuklasan na kung saan pinaniniwalaang dito inilalagay ang buto ng mga yumao?", "Monggul Jar", "Monunggul Jar", "Manunggul Jar", "Mang Jar", "Monunggul Jar"));
        questionsHard2.add(new QuestionModel("Tawag sa sinaunang namumuno sa mga barangay.", "Datu", "Rajah", "Lakan", "Lapu-lapu", "Datu"));
        questionsHard2.add(new QuestionModel("Ito ay isang sinaunang komersyo na matatagpuan sa Hilagang bahagi ng Luzon.", "Pateros", "Tondo", "Bataan", "Malate", "Tondo"));
        questionsHard2.add(new QuestionModel("Kailan nahulog ang Bataan sa kamay ng mga Hapones?", "Abril 9, 1942", "Mayo 6, 1942", "Enero 2, 1942", "Abril 4, 1942","Abril 9, 1942"));
        questionsHard2.add(new QuestionModel("Ang Unang Republika ng Pilipinas ay itinatag noong panahon ng digmaan. Alin?", "Digmaang Espanyol-Amerikano", "Rebolusyong Pilipino", "Unang Digmaang Pandaigdig", "Digmaang Pilipino-Amerikano", "Digmaang Pilipino-Amerikano"));
        questionsHard2.add(new QuestionModel("Ang asawa ni Diego Silang, na nagpatuloy sa pag-aalsa laban sa mga Kastila sa rehiyon ng Ilocos pagkamatay ni Diego?", "Luciana", "Geronima", "Gabriela", "Manuela", "Gabriela"));
        questionsHard2.add(new QuestionModel("Nanguna ang Filipino president na ito sa 1939 Bar Examinations (with near-perfect score) sa kabila ng pagkakakulong ng 27 araw. Sino siya?", "Ninoy Aquino", "Ferdinand Marcos", "Diosdado Macapagal", "Carlos Garcia", "Ferdinand Marcos"));
        questionsHard2.add(new QuestionModel("Kasama ni Gen. Fidel Ramos, ang taong ito ay tumulong sa pagpapatalsik kay Marcos noong 1986 na walang dugong rebolusyon. Sino siya?", "Gregorio Honasan", "Juan Ponce Enrile", "Fabian Ver", "Ninoy Aquino", "Juan Ponce Enrile"));
        questionsHard2.add(new QuestionModel("Sino ang sumulat ng \"Poster\", itinuturing na 'bibliya' ng kilusang Katipunan?", "Emilio Jacinto", "Andres Bonifacio", "Apolinario Mabini", "Valentin Diaz", "Emilio Jacinto"));
        questionsHard2.add(new QuestionModel("Ang pangalan ng sundalong Pilipino na namuno sa Cavite Mutiny noong 1872?", "Fr. Jose Burgos", "Sarhento Lamadrid", "Lt. Col. Taviel ng Andrade", "Gen. Mariano Noriel", "Sarhento Lamadrid"));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
    }
}
