package luanvan.luanvantotnghiep;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import luanvan.luanvantotnghiep.Activity.EquilibriumActivity;
import luanvan.luanvantotnghiep.Activity.SignInActivity;
import luanvan.luanvantotnghiep.CheckInternet.AsyncTaskListener;
import luanvan.luanvantotnghiep.CheckInternet.InternetCheck;
import luanvan.luanvantotnghiep.Fragment.AboutUsFragment;
import luanvan.luanvantotnghiep.Fragment.MainFragment;
import luanvan.luanvantotnghiep.Fragment.MethodEquilibriumFragment;
import luanvan.luanvantotnghiep.Fragment.PeriodicTableFragment;
import luanvan.luanvantotnghiep.Fragment.PickingClassFragment;
import luanvan.luanvantotnghiep.Fragment.ProfileFragment;
import luanvan.luanvantotnghiep.Fragment.RankFragment;
import luanvan.luanvantotnghiep.Fragment.ReactionFragment;
import luanvan.luanvantotnghiep.Fragment.ReactivitySeriesFragment;
import luanvan.luanvantotnghiep.Fragment.SearchFragment;
import luanvan.luanvantotnghiep.Fragment.SolubilityTableFragment;
import luanvan.luanvantotnghiep.Model.Rank;
import luanvan.luanvantotnghiep.Util.Constraint;
import luanvan.luanvantotnghiep.Util.PreferencesManager;
import luanvan.luanvantotnghiep.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager mManager;
    private FragmentTransaction mTransaction;
    private Toolbar mToolbarMain;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private Fragment mFragmentToSet = null;

    private int mCurrentId = R.id.nav_main;

    private boolean mIsPeriodic = false;

    private PreferencesManager mPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();

        init();

        setupNavigate();

        //Load MainFragment to MainActivity
        mTransaction.replace(R.id.container, MainFragment.newInstance());
        mTransaction.commit();

    }

    private void setupToolbar() {
        mToolbarMain = findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbarMain);

    }

    @SuppressLint("CommitTransaction")
    private void init() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mManager = getSupportFragmentManager();
        mTransaction = mManager.beginTransaction();

        mPreferencesManager = PreferencesManager.getInstance();
        mPreferencesManager.init(this);
    }

    private void setupNavigate() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbarMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                if (mFragmentToSet != null) {
                    mTransaction = mManager.beginTransaction();
                    mTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    mTransaction.replace(R.id.container, mFragmentToSet);
                    mTransaction.commit();
                }
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        mNavigationView.setItemIconTintList(null);

        //Set name and phone to header navigation
        View headerView = mNavigationView.getHeaderView(0);
        ImageView imgAvatarHeader = headerView.findViewById(R.id.img_avatar_user);
        TextView tvNameHeader = headerView.findViewById(R.id.tv_name_user);
        TextView tvPhoneHeader = headerView.findViewById(R.id.tv_phone_user);

        Glide.with(this).load(R.drawable.logo_app).into(imgAvatarHeader);

        tvNameHeader.setText(mPreferencesManager.getStringData(Constraint.PRE_KEY_NAME, "Xin chào"));
        tvPhoneHeader.setText(mPreferencesManager.getStringData(Constraint.PRE_KEY_PHONE, "Cẩm nang hóa học"));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {

            switchFragment(R.id.nav_main, MainFragment.newInstance());
            mToolbarMain.setTitle("Trò chơi");

        } else if (id == R.id.nav_periodic_table) {

            switchFragment(R.id.nav_periodic_table, PeriodicTableFragment.newInstance());
            mToolbarMain.setTitle("Bảng tuần hoàn");

        } else if (id == R.id.nav_solubility_table) {

            switchFragment(R.id.nav_solubility_table, SolubilityTableFragment.newInstance());
            mToolbarMain.setTitle("Bảng tính tan");

        } else if (id == R.id.nav_reactivity_series) {

            switchFragment(R.id.nav_reactivity_series, ReactivitySeriesFragment.newInstance());
            mToolbarMain.setTitle("Dãy hoạt động của kim loại");

        } else if (id == R.id.nav_theory) {

            switchFragment(R.id.nav_theory, PickingClassFragment.newInstance());
            mToolbarMain.setTitle("Các chuyên đề");

        } else if (id == R.id.nav_search) {

            switchFragment(R.id.nav_search, SearchFragment.newInstance());
            mToolbarMain.setTitle("Tìm kiếm chất hóa học");

        } else if (id == R.id.nav_reaction) {

            switchFragment(R.id.nav_reaction, ReactionFragment.newInstance());
            mToolbarMain.setTitle("Tìm kiếm phương trình hóa học");

        } else if (id == R.id.nav_rank) {
            InternetCheck internetCheck = new InternetCheck();
            internetCheck.setListener(new AsyncTaskListener() {
                @Override
                public void passResultInternet(Boolean internet) {
                    if (internet) {
                        pushDataScore();
                        switchFragment(R.id.nav_rank, RankFragment.newInstance());
                    } else {
                        Toast.makeText(MainActivity.this, "Vui lòng kiểm tra mạng!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            internetCheck.execute();
            mToolbarMain.setTitle("Xếp hạng");

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            mPreferencesManager.saveStringData(Constraint.PRE_KEY_PHONE_ENCODE, "");
            mPreferencesManager.saveStringData(Constraint.PRE_KEY_PASS_ENCODE, "");
            mPreferencesManager.saveIntData(Constraint.KEY_GAME, 0);
            mPreferencesManager.saveIntData(Constraint.KEY_THEMATIC, 0);
            mToolbarMain.setTitle("Đăng xuất");
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        } else if (id == R.id.nav_equilibrium) {

            switchFragment(R.id.nav_equilibrium, MethodEquilibriumFragment.newInstance());
            mToolbarMain.setTitle("Các phương pháp cân bằng");

        } else if (id == R.id.nav_profile) {

            switchFragment(R.id.nav_profile, ProfileFragment.newInstance());
            mToolbarMain.setTitle("Thông tin cá nhân");

        } else if (id == R.id.nav_about) {

            switchFragment(R.id.nav_about, AboutUsFragment.newInstance());
            mToolbarMain.setTitle("Nhà phát triển");
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchFragment(int id, Fragment fragment) {

        if (mCurrentId == id) {
            mFragmentToSet = null;
        } else {
            mFragmentToSet = fragment;
            mCurrentId = id;
        }
    }

    @SuppressLint("NewApi")
    private String encodeSHA512(String text, int extent) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            if (extent == 1) {
                md.update(Constraint.SLAT_RANK_EASY.getBytes(StandardCharsets.UTF_8));
            } else if (extent == 2) {
                md.update(Constraint.SLAT_RANK_NORMAL.getBytes(StandardCharsets.UTF_8));
            } else if (extent == 3) {
                md.update(Constraint.SLAT_RANK_DIFFICULT.getBytes(StandardCharsets.UTF_8));
            }
            byte[] bytes = md.digest(text.getBytes(StandardCharsets.UTF_8));
            for (byte aDigest : bytes) {
                //sb.append(Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1));
                sb.append(String.format("%02x", aDigest));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void pushDataScore() {
        String phone = mPreferencesManager.getStringData(Constraint.PRE_KEY_PHONE, "");
        final int block = mPreferencesManager.getIntData(Constraint.PRE_KEY_BLOCK, 8);
        final String name = mPreferencesManager.getStringData(Constraint.PRE_KEY_NAME, "");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference("RANK");

        if (!phone.equals("") && !name.equals("")) {
            float scoreEasy = mPreferencesManager.getFloatData(Constraint.PRE_KEY_RANK_EASY, 0);
            String keyEasy = block + encodeSHA512(phone, Constraint.EXTENT_EASY);
            Rank rankEasy = new Rank();
            rankEasy.setBlock(block);
            rankEasy.setExtent(Constraint.EXTENT_EASY);
            rankEasy.setName(name);
            rankEasy.setScore(scoreEasy);
            reference.child(keyEasy).setValue(rankEasy);

            float scoreNormal = mPreferencesManager.getFloatData(Constraint.PRE_KEY_RANK_NORMAL, 0);
            String keyNormal = block + encodeSHA512(phone, Constraint.EXTENT_NORMAL);
            Rank rankNormal = new Rank();
            rankNormal.setBlock(block);
            rankNormal.setExtent(Constraint.EXTENT_NORMAL);
            rankNormal.setName(name);
            rankNormal.setScore(scoreNormal);
            reference.child(keyNormal).setValue(rankNormal);

            float scoreDifficult = mPreferencesManager.getFloatData(Constraint.PRE_KEY_RANK_DIFFICULT, 0);
            String keyDifficult = block + encodeSHA512(phone, Constraint.EXTENT_DIFFICULT);
            Rank rankDifficult = new Rank();
            rankDifficult.setBlock(block);
            rankDifficult.setExtent(Constraint.EXTENT_DIFFICULT);
            rankDifficult.setName(name);
            rankDifficult.setScore(scoreDifficult);
            reference.child(keyDifficult).setValue(rankDifficult);
        }
    }
}
