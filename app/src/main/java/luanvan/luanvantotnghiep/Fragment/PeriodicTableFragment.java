package luanvan.luanvantotnghiep.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import luanvan.luanvantotnghiep.Adapter.SwipeAdapter;
import luanvan.luanvantotnghiep.Database.ChemistryHelper;
import luanvan.luanvantotnghiep.Model.Chemistry;
import luanvan.luanvantotnghiep.Model.Element;
import luanvan.luanvantotnghiep.Model.Group;
import luanvan.luanvantotnghiep.R;
import luanvan.luanvantotnghiep.Util.ChemistrySingle;

public class PeriodicTableFragment extends Fragment {

    private static final int[] LIST_COLORS = new int[]{
            Color.parseColor("#ff6666"),
            Color.parseColor("#ffdead"),
            Color.parseColor("#cccccc"),
            Color.parseColor("#cccc99"),
            Color.parseColor("#ffc0c0"),
            Color.parseColor("#a0ffa0"),
            Color.parseColor("#ffff99"),
            Color.parseColor("#c0ffff"),
            Color.parseColor("#ffbfff"),
            Color.parseColor("#ff99cc"),
            Color.parseColor("#e8e8e8")
    };

    //Width and Height item_periodic
    private static final int mWidth = 120; //120
    private static final int mHeight = 160; //190

    private List<Element> mElementList;
    private List<Group> mGroupList;
    private Context mContext;

    private TableLayout mTlPeriodic;
    private TextView tvId;
    private TextView tvSymbol;
    private TextView tvName;

    private ChemistryHelper mChemistryHelper;

    private int mIDType = 0;

    public PeriodicTableFragment() {
    }

    public static PeriodicTableFragment newInstance() {
        return new PeriodicTableFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_periodic_table, container, false);

        init(view);

        showPeriodicTable(mIDType);

        return view;
    }

    private void init(View v) {
        mGroupList = new ArrayList<>();
        mElementList = new ArrayList<>();
        mTlPeriodic = v.findViewById(R.id.tl_periodic);

        mChemistryHelper = ChemistrySingle.getInstance(getContext());
        mGroupList = mChemistryHelper.getAllGroups();
        mElementList = mChemistryHelper.getAllElements();
    }

    private void createPeriodicTable(int idType) {
        TableRow header = new TableRow(mContext);

        LayoutInflater l = getLayoutInflater();
        View view;

        //Row one set group IA and VIIIA
        for (int i = 0; i < 19; i++) {

            view = inflateView(l);
            if (i == 1 || i == 18) {
                tvSymbol.setText(mGroupList.get(i - 1).getNameGroup());
                tvSymbol.setTextSize(20);
                tvSymbol.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            } else {
                tvSymbol.setText("");
            }
            tvId.setText("");
            tvName.setText("");
            if (i == 0 || i == 18) {
                view.setBackgroundResource(R.drawable.backgroud_item_empty_first_lantan_3a8a);
            }
            if (i == 1) {
                view.setBackgroundResource(R.drawable.backgroud_item_group);
            }
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            header.addView(view, mWidth, mHeight);
        }
        mTlPeriodic.addView(header);

        //Period 1 - 7
        for (int i = 1; i < 8; i++) {

            //Number of periodic
            TableRow row = new TableRow(mContext);
            view = inflateView(l);
            tvId.setText("");
            tvName.setText("");
            tvSymbol.setText(String.format("%s", i));
            view.setBackgroundResource(R.drawable.backgroud_item_cycle_first_actini);
            row.addView(view, mWidth, mHeight);

            //Value row periodic
            for (int j = 1; j < 19; j++) {
                view = inflateView(l);
                final int position = checkPeriodicEmpty(i, j);

                //Condition: cell EMPTY or HAVE value
                if (position != -1) {
                    Element element = mElementList.get(position);
                    tvId.setText(String.valueOf(element.getIdElement()));
                    Chemistry chemistry = mChemistryHelper.getChemistryById(element.getIdElement());
                    tvSymbol.setText(chemistry.getSymbolChemistry());
                    tvName.setText(chemistry.getNameChemistry());
                    //view.findViewById(R.id.ln_bg).setBackgroundColor(LIST_COLORS[chemistry.getIdType() - 1]);

                    if (idType == chemistry.getIdType() || idType == 0) {
                        view.findViewById(R.id.ln_bg).setBackgroundColor(LIST_COLORS[chemistry.getIdType() - 1]);
                    }

                    //setColorStateOfMatter(tvId, chemistry);
                    setColorElementCategory(idType, tvId, chemistry, view);
                    LinearLayout layout = view.findViewById(R.id.ln_item);
                    layout.setBackgroundResource(R.drawable.background_item_chat_hoa_hoc);

                    //Bo: position = 4 and value id = 5
                    if (position == 4) {
                        layout.setBackgroundResource(R.drawable.backgroud_item_cycle_first_actini);
                    }

                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDialogInfo(position);
                        }
                    });

                } else {
                    tvId.setText("");
                    tvName.setText("");
                    //Condition groups
                    if (i == 1 && (j < 3 || j > 12)
                            || i == 3
                            && (j > 2 && j < 13)) {

                        //Condition IIIA
                        if (i == 1 && j == 13) {
                            view.setBackgroundResource(R.drawable.backgroud_item_empty_first_lantan_3a8a);
                        } else {
                            view.setBackgroundResource(R.drawable.backgroud_item_group);
                        }
                        tvSymbol.setText(mGroupList.get(j - 1).getNameGroup());
                        tvSymbol.setTextSize(20);
                        tvSymbol.setGravity(Gravity.BOTTOM | Gravity.CENTER);
                    } else {
                        tvSymbol.setText("");
                    }
                }
                row.addView(view, mWidth, mHeight);
            }
            mTlPeriodic.addView(row);
        }
    }

    //Return -1 if Element is EMPTY
    private int checkPeriodicEmpty(int period, int positionShow) {

        for (int i = 0; i < mElementList.size(); i++) {
            Element element = mElementList.get(i);
            if (element.getPeriod() == period && element.getIdGroup() == positionShow) {
                return i;
            }
        }
        return -1;
    }

    private void createRelative(int idType) {

        TableRow row = new TableRow(mContext);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        LayoutInflater li = getLayoutInflater();
        View v;

        //Create 3 space cell
        for (int i = 0; i < 3; i++) {
            v = inflateView(li);
            tvId.setText("");
            tvName.setText("");
            tvSymbol.setText("");
            row.addView(v, mWidth, mHeight);
        }

        //Create cell "LanTan"
        v = inflateView(li);
        tvId.setText("");
        tvName.setText("");
        tvSymbol.setText("Họ\nLantan");
        tvSymbol.setTextSize(16);
        //v.findViewById(R.id.ln_bg).setBackgroundColor(LIST_COLORS[8]);
        if (idType == 9 || idType == 0) {
            v.findViewById(R.id.ln_bg).setBackgroundColor(LIST_COLORS[8]);
        }
        v.setBackgroundResource(R.drawable.backgroud_item_empty_first_lantan_3a8a);
        row.addView(v, mWidth, mHeight);

        //Data lantan
        for (int i = 57; i < 71; i++) {
            v = inflateView(li);
            Element element = mElementList.get(i);
            tvId.setText(String.valueOf(element.getIdElement()));
            Chemistry chemistry = mChemistryHelper.getChemistryById(element.getIdElement());
            tvSymbol.setText(chemistry.getSymbolChemistry());
            tvName.setText(chemistry.getNameChemistry());
            LinearLayout layout = v.findViewById(R.id.ln_item);
            layout.setBackgroundResource(R.drawable.backgroud_item_group);
            //v.findViewById(R.id.ln_bg).setBackgroundColor(LIST_COLORS[chemistry.getIdType() - 1]);
            if (idType == 9 || idType == 0) {
                v.findViewById(R.id.ln_bg).setBackgroundColor(LIST_COLORS[chemistry.getIdType() - 1]);
            }
            //setColorStateOfMatter(tvId, chemistry);
            setColorElementCategory(idType, tvId, chemistry, v);

            final int position = i;
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogInfo(position);
                }
            });

            row.addView(v, mWidth, mHeight);
        }

        mTlPeriodic.addView(row);

        row = new TableRow(mContext);

        //Create 3 space cell
        for (int i = 0; i < 3; i++) {
            v = inflateView(li);
            tvId.setText("");
            tvName.setText("");
            tvSymbol.setText("");
            row.addView(v, mWidth, mHeight);
        }

        //Create cell "Actini"
        v = inflateView(li);
        tvId.setText("");
        tvName.setText("");
        tvSymbol.setText("Họ\nActini");
        tvSymbol.setTextSize(16);
        //v.findViewById(R.id.ln_bg).setBackgroundColor(LIST_COLORS[9]);
        if (idType == 10 || idType == 0) {
            v.findViewById(R.id.ln_bg).setBackgroundColor(LIST_COLORS[9]);
        }
        v.setBackgroundResource(R.drawable.backgroud_item_cycle_first_actini);
        row.addView(v, mWidth, mHeight);

        //Data Actini
        for (int i = 89; i < 103; i++) {
            v = inflateView(li);
            Element element = mElementList.get(i);
            tvId.setText(String.valueOf(element.getIdElement()));
            Chemistry chemistry = mChemistryHelper.getChemistryById(element.getIdElement());
            tvSymbol.setText(chemistry.getSymbolChemistry());
            tvName.setText(chemistry.getNameChemistry());
            LinearLayout layout = v.findViewById(R.id.ln_item);
            layout.setBackgroundResource(R.drawable.background_item_chat_hoa_hoc);
            //v.findViewById(R.id.ln_bg).setBackgroundColor(LIST_COLORS[chemistry.getIdType() - 1]);
            if (idType == 10 || idType == 0) {
                v.findViewById(R.id.ln_bg).setBackgroundColor(LIST_COLORS[chemistry.getIdType() - 1]);
            }
            //setColorStateOfMatter(tvId, chemistry);
            setColorElementCategory(idType, tvId, chemistry, v);

            final int position = i;
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogInfo(position);
                }
            });

            row.addView(v, mWidth, mHeight);
        }

        mTlPeriodic.addView(row);

    }

    private View inflateView(LayoutInflater li) {
        View v = li.inflate(R.layout.item_element, null);

        tvId = (TextView) v.findViewById(R.id.tv_id_element);
        tvSymbol = (TextView) v.findViewById(R.id.tv_symbol_element);
        tvName = (TextView) v.findViewById(R.id.tv_name_element);

        return v;
    }

    private void showPeriodicTable(int idType) {

        createPeriodicTable(idType);

        createSpaceLine();

        createRelative(idType);

        createSpaceLine();
    }

    private void createSpaceLine() {
        TableRow row = new TableRow(mContext);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        LayoutInflater vi = getLayoutInflater();
        View v = vi.inflate(R.layout.item_element, null);

        TextView tvId = (TextView) v.findViewById(R.id.tv_id_element);
        TextView tvKyHieu = (TextView) v.findViewById(R.id.tv_symbol_element);
        TextView tvTen = (TextView) v.findViewById(R.id.tv_name_element);

        tvId.setText("");
        tvKyHieu.setText("");
        tvTen.setText("");
        row.addView(v, mWidth, mHeight);
        mTlPeriodic.addView(row);
    }

    private void showDialogInfo(int position) {

        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.context_dialog_periodic);

        ViewPager viewPager;
        viewPager = dialog.findViewById(R.id.view_pager);

        SwipeAdapter adapter = new SwipeAdapter(mContext, mElementList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    private void setColorStateOfMatter(TextView textView, Chemistry chemistry) {
        String status = chemistry.getStatusChemistry();
        switch (status) {
            case "Rắn":
                textView.setTextColor(Color.BLACK);
                break;
            case "Lỏng":
                textView.setTextColor(Color.CYAN);
                break;
            case "Khí":
                textView.setTextColor(Color.RED);
                break;
            case "Chưa xác định":
                textView.setTextColor(Color.GRAY);
                break;
        }
    }

    private void setColorElementCategory(int idType, TextView textView, Chemistry chemistry, View view) {
        String status = chemistry.getStatusChemistry();
        switch (idType) {
            case 0:
                setColorStateOfMatter(textView, chemistry);
                break;

            case 12:
                if (status.equals("Rắn")) {
                    textView.setTextColor(Color.BLACK);
                    view.findViewById(R.id.ln_bg).setBackgroundColor(Color.GREEN);
                } else {
                    view.findViewById(R.id.ln_bg).setBackgroundColor(Color.WHITE);
                }
                break;

            case 13:
                if (status.equals("Lỏng")) {
                    textView.setTextColor(Color.BLACK);
                    view.findViewById(R.id.ln_bg).setBackgroundColor(Color.CYAN);
                } else {
                    view.findViewById(R.id.ln_bg).setBackgroundColor(Color.WHITE);
                }
                break;

            case 14:
                if (status.equals("Khí")) {
                    textView.setTextColor(Color.BLACK);
                    view.findViewById(R.id.ln_bg).setBackgroundColor(Color.RED);
                } else {
                    view.findViewById(R.id.ln_bg).setBackgroundColor(Color.WHITE);
                }
                break;

            case 15:
                if (status.equals("Chưa xác định")) {
                    textView.setTextColor(Color.BLACK);
                    view.findViewById(R.id.ln_bg).setBackgroundColor(Color.GRAY);
                } else {
                    view.findViewById(R.id.ln_bg).setBackgroundColor(Color.WHITE);
                }
                break;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_periodic_table, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_all:
                reloadFragment(0);
                showCustomToast(item.getTitle().toString(), 0);
                return true;

            case R.id.nav_alkali_metal:
                reloadFragment(1);
                showCustomToast(item.getTitle().toString(), 1);
                return true;

            case R.id.nav_alkaline_earth_metal:
                reloadFragment(2);
                showCustomToast(item.getTitle().toString(), 2);
                return true;

            case R.id.nav_post_transition_metal:
                reloadFragment(3);
                showCustomToast(item.getTitle().toString(), 3);
                return true;

            case R.id.nav_metalloid:
                reloadFragment(4);
                showCustomToast(item.getTitle().toString(), 4);
                return true;

            case R.id.nav_transition_metal:
                reloadFragment(5);
                showCustomToast(item.getTitle().toString(), 5);
                return true;

            case R.id.nav_nonmetal:
                reloadFragment(6);
                showCustomToast(item.getTitle().toString(), 6);
                return true;

            case R.id.nav_halogen:
                reloadFragment(7);
                showCustomToast(item.getTitle().toString(), 7);
                return true;

            case R.id.nav_noble_gas:
                reloadFragment(8);
                showCustomToast(item.getTitle().toString(), 8);
                return true;

            case R.id.nav_lanthanide:
                reloadFragment(9);
                showCustomToast(item.getTitle().toString(), 9);
                return true;

            case R.id.nav_actinide:
                reloadFragment(10);
                showCustomToast(item.getTitle().toString(), 10);
                return true;

            case R.id.nav_unknown_chemical_properties:
                reloadFragment(11);
                showCustomToast(item.getTitle().toString(), 11);
                return true;

            case R.id.nav_solid:
                reloadFragment(12);
                showCustomToast(item.getTitle().toString(), 12);
                return true;

            case R.id.nav_liquid:
                reloadFragment(13);
                showCustomToast(item.getTitle().toString(), 13);
                return true;

            case R.id.nav_gas:
                reloadFragment(14);
                showCustomToast(item.getTitle().toString(), 14);
                return true;

            case R.id.nav_unknown:
                reloadFragment(15);
                showCustomToast(item.getTitle().toString(), 15);
                return true;

            default:
                return false;
        }
    }

    private void reloadFragment(int type) {
        mIDType = type;
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    private void showCustomToast(String title, int type) {
        Toast toast = Toast.makeText(mContext, title, Toast.LENGTH_LONG);
        View toastView = toast.getView();
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);

        if (type == 0) {
            toastView.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            toastMessage.setBackgroundColor(Color.BLACK);
        } else if (type < 12) {
            toastView.getBackground().setColorFilter(LIST_COLORS[type - 1], PorterDuff.Mode.SRC_IN);
            toastMessage.setBackgroundColor(LIST_COLORS[type - 1]);
        } else {
            int color = Color.TRANSPARENT;
            if (type == 12) {
                color = Color.GREEN;
            } else if (type == 13) {
                color = Color.CYAN;
            } else if (type == 14) {
                color = Color.RED;
            } else if (type == 15) {
                color = Color.GRAY;
            }
            toastView.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            toastMessage.setBackgroundColor(color);
        }
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setGravity(Gravity.CENTER);
        toast.show();
    }
}
