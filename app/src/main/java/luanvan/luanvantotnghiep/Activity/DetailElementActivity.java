package luanvan.luanvantotnghiep.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import luanvan.luanvantotnghiep.Database.ChemistryHelper;
import luanvan.luanvantotnghiep.Model.Chemistry;
import luanvan.luanvantotnghiep.Model.Element;
import luanvan.luanvantotnghiep.Model.Group;
import luanvan.luanvantotnghiep.Model.Type;
import luanvan.luanvantotnghiep.R;
import luanvan.luanvantotnghiep.Util.ChemistrySingle;
import luanvan.luanvantotnghiep.View.ElectronView;

public class DetailElementActivity extends AppCompatActivity implements View.OnClickListener {

    private ChemistryHelper mHelper;

    private Toolbar mToolbar;

    private ImageView mImgBackground;
    private ImageView mImgWiki;

    private TextView mTvElectron;
    private TextView mTvProton;
    private TextView mTvNeutron;

    private TextView mTvElementCategory;
    private TextView mTvWeight;
    private TextView mTvEnglishName;
    private TextView mTvGroup;
    private TextView mTvPeriodic;
    private TextView mTvElectronegativity;
    private TextView mTvWikiPaulingScale;
    private TextView mTvValence;

    private TextView mTvClass;
    private TextView mTvSimplified;
    private TextView mTvConfiguration;
    private ElectronView mElectronView;

    private TextView mTvStatus;
    private TextView mTvColor;
    private TextView mTvIsotope;
    private ImageView mImgWikiTableIsotope;
    private TextView mTvMeltingPoint;
    private TextView mTvBoilingPoint;

    private TextView mTvDiscoverer;
    private TextView mTvYearDiscovery;

    private String mConfig;
    private String mShell;

    private Element element;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_element);

        setupToolbar();

        init();

        showInfo();

        addEvents();

    }

    private void setupToolbar() {
        mToolbar = findViewById(R.id.toolbar_detail_element);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addEvents() {
        mImgWiki.setOnClickListener(this);
        mTvWikiPaulingScale.setOnClickListener(this);
        mImgWikiTableIsotope.setOnClickListener(this);
        mElectronView.setOnClickListener(this);
    }

    private void init() {

        mHelper = ChemistrySingle.getInstance(this);

        mImgBackground = findViewById(R.id.img_background);
        mImgWiki = findViewById(R.id.img_wiki);

        mTvElectron = findViewById(R.id.tv_electron);
        mTvProton = findViewById(R.id.tv_proton);
        mTvNeutron = findViewById(R.id.tv_neutron);

        mTvElementCategory = findViewById(R.id.tv_element_category);
        mTvWeight = findViewById(R.id.tv_weight_chemical);
        mTvEnglishName = findViewById(R.id.tv_english_name);
        mTvGroup = findViewById(R.id.tv_group);
        mTvPeriodic = findViewById(R.id.tv_period);
        mTvElectronegativity = findViewById(R.id.tv_electronegativity);
        mTvWikiPaulingScale = findViewById(R.id.tv_wiki_pauling_scale);
        mTvValence = findViewById(R.id.tv_valence);

        mTvClass = findViewById(R.id.tv_class);
        mTvSimplified = findViewById(R.id.tv_simplified_configuration);
        mTvConfiguration = findViewById(R.id.tv_configuration);
        mElectronView = findViewById(R.id.electron_view);

        mTvStatus = findViewById(R.id.tv_status_chemical);
        mTvColor = findViewById(R.id.tv_color_chemical);
        mTvIsotope = findViewById(R.id.tv_isotope);
        mImgWikiTableIsotope = findViewById(R.id.img_wiki_table_isotope);
        mTvMeltingPoint = findViewById(R.id.tv_melting_point);
        mTvBoilingPoint = findViewById(R.id.tv_boiling_point);

        mTvDiscoverer = findViewById(R.id.tv_discoverer);
        mTvYearDiscovery = findViewById(R.id.tv_year_discovery);

    }

    private String getNameTypeById(int id) {
        List<Type> typeList = mHelper.getAllTypes();
        for (Type type : typeList) {
            if (type.getIdType() == id) {
                return type.getNameType();
            }
        }
        return "";
    }

    private void showInfo() {
        Intent intent = getIntent();

        if (intent != null) {

            element = (Element) intent.getSerializableExtra("ELEMENT_INTENT");
            Chemistry chemistry = (Chemistry) intent.getSerializableExtra("CHEMISTRY_INTENT");

            if (element != null && chemistry != null) {
                int resID = getResources().getIdentifier(element.getPicture(), "drawable", getPackageName());
                Glide.with(this)
                        .load(resID)
                        .into(mImgBackground);

                mTvElectron.setText(String.valueOf(element.getIdElement()));
                mTvProton.setText(String.valueOf(element.getIdElement()));
                mTvNeutron.setText(String.valueOf(element.getNeutron()));

                mTvElementCategory.setText(Html.fromHtml("<font color='gray'>Phân loại: </font><font color='black'>" + String.valueOf(getNameTypeById(chemistry.getIdType())) + "</font>"));
                mTvWeight.setText(Html.fromHtml("<font color='gray'>Khối lượng: </font><font color='black'>" + String.valueOf(chemistry.getWeightChemistry()) + " (g/mol)</font>"));
                mTvEnglishName.setText(Html.fromHtml("<font color='gray'>Tên Tiếng Anh: </font><font color='black'>" + element.getEnglishName() + "</font>"));
                mTvPeriodic.setText(Html.fromHtml("<font color='gray'>Chu kỳ: </font><font color='black'>" + element.getPeriod() + "</font>"));
                mTvElectronegativity.setText(Html.fromHtml("<font color='gray'>Độ âm điện: </font><font color='black'>" + String.valueOf(element.getElectronegativity()) + "</font>"));
                mTvValence.setText(Html.fromHtml("<font color='gray'><b>Hóa trị: </b></font><font color='black'>" + element.getValence() + "</font>"));

                mTvClass.setText(Html.fromHtml("<font color='gray'>Phân lớp: </font><font color='black'>" + element.getClassElement() + "</font>"));
                mTvSimplified.setText(Html.fromHtml("<font color='gray'>Electron rút rọn: </font><font color='black'>" + element.getSimplifiedConfiguration() + "</font>"));

                mTvStatus.setText(Html.fromHtml("<font color='gray'>Trạng thái: </font><font color='black'>" + chemistry.getStatusChemistry() + "</font>"));
                mTvColor.setText(Html.fromHtml("<font color='gray'>Màu sắc: </font><font color='black'>" + chemistry.getColorChemistry() + "</font>"));
                mTvIsotope.setText(Html.fromHtml("<font color='gray'>Đồng vị (bền): </font><font color='black'>" + element.getIsotopes() + "</font>"));

                mTvMeltingPoint.setText(Html.fromHtml("<font color='gray'>Nhiệt độ nóng chảy: </font><font color='black'>" + String.valueOf(element.getMeltingPoint() + "°C") + "</font>"));
                mTvBoilingPoint.setText(Html.fromHtml("<font color='gray'>Nhiệt độ sội: </font><font color='black'>" + String.valueOf(element.getBoilingPoint() + "°C") + "</font>"));

                String discoverer = element.getDiscoverer();
                mTvDiscoverer.setText(Html.fromHtml("<font color='gray'>Khám phá bởi:  </font><font color='black'>" + discoverer + "</font>"));
                if (discoverer.equals("")) {
                    mTvDiscoverer.setVisibility(View.GONE);
                }
                mTvYearDiscovery.setText(Html.fromHtml("<font color='gray'>Năm khám phá: </font><font color='black'>" + element.getYearDiscovery() + "</font>"));

                //Handel config HTML
                String config = element.getConfiguration();
                mTvConfiguration.setText(Html.fromHtml("<font color='gray'>Cấu hình electron: </font><font color='black'>" + handelConfigElectron(config) + "</font>"));
                mConfig = config;

                mShell = element.getShell();
                mElectronView.setShellToView(mShell);

                List<Group> list = mHelper.getAllGroups();

                String group = "";
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getIdGroup() == element.getIdGroup()) {
                        group = list.get(i).getNameGroup();
                        break;
                    }
                }
                mTvGroup.setText(Html.fromHtml("<font color='gray'>Nhóm:  </font><font color='black'>" + group + "</font>"));

                //mToolbar.setTitle(chemistry.getNameChemistry());
                getSupportActionBar().setTitle(chemistry.getNameChemistry());


            } else {
                Log.i("ANTN", "Null!");
            }
        }
    }

    private String handelConfigElectron(String config) {
        StringBuilder result = new StringBuilder();
        final String itemConfig[] = config.split(" ");
        for (String anItemConfig : itemConfig) {
            String number = anItemConfig.substring(0, 1);
            String shell = anItemConfig.substring(1, 2);
            String orbital = anItemConfig.substring(2);

            result.append(number).append(shell).append("<small><sup>")
                    .append(orbital).append("</sup></small>  ");
        }
        return result.toString();
    }

    @Override
    public void onClick(View view) {
        String nameEnglish;
        if (!element.getEnglishName().equals("Mercury")) {
            nameEnglish = element.getEnglishName();
        } else {
            nameEnglish = "Mercury_(element)";
        }

        String uri;
        Intent intent;
        switch (view.getId()) {
            case R.id.img_wiki:
                uri = "https://en.wikipedia.org/wiki/" + nameEnglish;
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                break;

            case R.id.tv_wiki_pauling_scale:
                uri = "https://en.wikipedia.org/wiki/Electronegativities_of_the_elements_(data_page)";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                break;

            case R.id.img_wiki_table_isotope:
                uri = "https://en.wikipedia.org/wiki/Table_of_nuclides_(combined)";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                break;

            case R.id.electron_view:
                intent = new Intent(this, ConfigElectronActivity.class);
                intent.putExtra("CONFIG", mConfig);
                intent.putExtra("SHELL", mShell);

                startActivity(intent);
                break;
        }

    }

}
