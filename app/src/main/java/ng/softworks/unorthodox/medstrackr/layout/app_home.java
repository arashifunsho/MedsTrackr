package ng.softworks.unorthodox.medstrackr.layout;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import ng.softworks.unorthodox.medstrackr.R;


public class app_home extends Fragment {

    @Bind(R.id.lLayoutHome) LinearLayout bgLayout;

    public static final String TAG = "app_home";

    public app_home() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_app_home, container, false);

        ButterKnife.bind(this,view);

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int color1= R.color.Blue; int color2=R.color.Red;int color3=R.color.Green;int color4=R.color.myPrimaryColor; int color5= R.color.White;
        final ValueAnimator bgAnim= ValueAnimator.ofObject(new ArgbEvaluator(),color4,color5);
        bgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bgLayout.setBackgroundColor((int) bgAnim.getAnimatedValue());
            }
        });

        bgAnim.setDuration(15000);
        bgAnim.setRepeatMode(bgAnim.REVERSE);
        bgAnim.setRepeatCount(bgAnim.INFINITE);
        bgAnim.start();

    }
    //===============SET FRAGMENTS MENU ITEMS===============================================

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
    //======================================================================================


}
