package luanvan.luanvantotnghiep.Activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import luanvan.luanvantotnghiep.Adapter.RecyclerViewQuestionAdapter;
import luanvan.luanvantotnghiep.Helper.StartSnapHelper;
import luanvan.luanvantotnghiep.Model.Answer;
import luanvan.luanvantotnghiep.Model.AnswerByQuestion;
import luanvan.luanvantotnghiep.Model.Question;
import luanvan.luanvantotnghiep.R;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mTvTime;
    private Button mBtnComplete;
    private RecyclerViewQuestionAdapter mRecyclerViewQuestionAdapter;
    private RecyclerView mRvQuestion;

    private List<Question> mQuestionList;
    private List<Answer> mAnswerList;
    private List<AnswerByQuestion> mAnswerByQuestionList;

    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        setupToolbar();

        init();

        addDataQuestion();

        addDataAnswer();

        addDataAnswerByQuestion();

        setUpTime();

        showQuestion();
    }

    private void init() {
        mTvTime = findViewById(R.id.tv_time);
        mRvQuestion = findViewById(R.id.rv_question);

        mBtnComplete = findViewById(R.id.btn_complete_quiz);
        mBtnComplete.setOnClickListener(this);
    }

    private void addDataQuestion() {
        mQuestionList = new ArrayList<>();
        Question question;

        question = new Question(1, "Chỉ ra dãy nào chỉ gồm toàn là vật thể tự nhiên?");
        mQuestionList.add(question);

        question = new Question(2, "Chỉ ra dãy nào chỉ gồm toàn là vật thể nhân tạo?");
        mQuestionList.add(question);

        question = new Question(3, "Cho dãy các cụm từ sau, dãy nào dưới đây chỉ chất?");
        mQuestionList.add(question);

        question = new Question(4, "Nước sông hồ thuộc loại:");
        mQuestionList.add(question);

        question = new Question(5, "Để tách rượu ra khỏi hỗn hợp rượu lẫn nước, dùng cách nào sau đây?");
        mQuestionList.add(question);

        question = new Question(6, "Những nhận xét nào sau đây đúng?");
        mQuestionList.add(question);
    }

    private void addDataAnswerByQuestion() {
        mAnswerByQuestionList = new ArrayList<>();
        AnswerByQuestion answerByQuestion;

        answerByQuestion = new AnswerByQuestion(1, 1, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(1, 2, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(1, 3, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(1, 4, true);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(2, 1, true);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(2, 2, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(2, 3, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(2, 4, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(3, 5, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(3, 6, true);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(3, 7, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(3, 8, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(4, 9, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(4, 10, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(4, 11, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(4, 12, true);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(5, 13, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(5, 14, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(5, 15, true);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(5, 16, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(6, 17, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(6, 18, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(6, 19, false);
        mAnswerByQuestionList.add(answerByQuestion);

        answerByQuestion = new AnswerByQuestion(6, 20, true);
        mAnswerByQuestionList.add(answerByQuestion);

    }

    private void addDataAnswer() {

        mAnswerList = new ArrayList<>();
        Answer answer;

        answer = new Answer(1, "Ấm nhôm, bình thủy tinh, nồi đất sét.");
        mAnswerList.add(answer);

        answer = new Answer(2, "Xenlulozơ, kẽm, vàng.");
        mAnswerList.add(answer);

        answer = new Answer(3, "Thao, bút, tập, sách.");
        mAnswerList.add(answer);

        answer = new Answer(4, "Nước biển, ao, hồ, suối.");
        mAnswerList.add(answer);

        answer = new Answer(5, "Bàn ghế, đường kính, vải may áo.");
        mAnswerList.add(answer);

        answer = new Answer(6, "Muối ăn, đường kính, bột sắt, nước cất.");
        mAnswerList.add(answer);

        answer = new Answer(7, "Bút chì, thước kẻ, nước cất, vàng.");
        mAnswerList.add(answer);

        answer = new Answer(8, "Nhôm, sắt, than củi, chảo gang.");
        mAnswerList.add(answer);

        answer = new Answer(9, "Đơn chất.");
        mAnswerList.add(answer);

        answer = new Answer(10, "Hợp chất.");
        mAnswerList.add(answer);

        answer = new Answer(11, "Chất tinh khiết.");
        mAnswerList.add(answer);

        answer = new Answer(12, "Hỗn hợp.");
        mAnswerList.add(answer);

        answer = new Answer(13, "Lọc.");
        mAnswerList.add(answer);

        answer = new Answer(14, "Dùng phễu chiết.");
        mAnswerList.add(answer);

        answer = new Answer(15, "Chưng cất phân đoạn.");
        mAnswerList.add(answer);

        answer = new Answer(16, "Đốt.");
        mAnswerList.add(answer);

        answer = new Answer(17, "Xăng, khí nitơ, muối ăn, nước tự nhiên là hỗn hợp.");
        mAnswerList.add(answer);

        answer = new Answer(18, "Sữa, không khí, nước chanh, trà đá là hợp chất.");
        mAnswerList.add(answer);

        answer = new Answer(19, "Muối ăn, đường, khí cacbonic, nước cất là chất tinh khiết.");
        mAnswerList.add(answer);

        answer = new Answer(20, "Dựa vào sự khác nhau về tính chất vật lý có thể tách một chất ra khỏi hỗn hợp.");
        mAnswerList.add(answer);
    }

    private void setupToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpTime() {
        mCountDownTimer = new CountDownTimer(1800000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTvTime.setText("" + String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                //nếu time < 5p đỏ mess: còn 5p làm bài.
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void showQuestion() {
        mRecyclerViewQuestionAdapter = new RecyclerViewQuestionAdapter(this,
                mQuestionList, mAnswerList, mAnswerByQuestionList);

        mRvQuestion.setAdapter(mRecyclerViewQuestionAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false);
        mRvQuestion.setLayoutManager(mLayoutManager);
        mRvQuestion.setHasFixedSize(true);

        SnapHelper startSnapHelper = new StartSnapHelper();
        startSnapHelper.attachToRecyclerView(mRvQuestion);
    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.btn_complete_quiz) {
//            Log.i("ANTN", "Score: " + mSwipeQuestionAdapter.getScore());
//            for (int i = 0; i < mSwipeQuestionAdapter.getCount(); i++) {
//
//                viewPager.setCurrentItem(i);
//            }
//            viewPager.setCurrentItem(0);
//        }
    }
}

