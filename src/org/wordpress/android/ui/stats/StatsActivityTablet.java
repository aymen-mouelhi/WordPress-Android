package org.wordpress.android.ui.stats;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.android.volley.VolleyError;
import com.wordpress.rest.RestRequest.ErrorListener;
import com.wordpress.rest.RestRequest.Listener;

import org.json.JSONObject;

import org.wordpress.android.R;
import org.wordpress.android.WordPress;
import org.wordpress.android.models.StatsSummary;
import org.wordpress.android.ui.WPActionBarActivity;
import org.wordpress.android.util.StatUtils;
import org.wordpress.android.util.Utils;

public class StatsActivityTablet extends WPActionBarActivity {

    private static final int TABLET_720DP = 720;
    private static final int TABLET_600DP = 600;
    private LinearLayout mFragmentContainer;
    private LinearLayout mColumnLeft;
    private LinearLayout mColumnRight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        if (WordPress.wpDB == null) {
            Toast.makeText(this, R.string.fatal_db_error, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        createMenuDrawer(R.layout.stats_activity_tablet);
        
        mFragmentContainer = (LinearLayout) findViewById(R.id.stats_fragment_container);
        mColumnLeft = (LinearLayout) findViewById(R.id.stats_tablet_col_left);
        mColumnRight = (LinearLayout) findViewById(R.id.stats_tablet_col_right); 
        
        loadStatsFragments();
    }

    private void loadStatsFragments() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        StatsAbsViewFragment fragment;
        
        if (fm.findFragmentByTag(StatsVisitorsAndViewsFragment.TAG) == null) {
            fragment = StatsAbsViewFragment.newInstance(StatsViewType.VISITORS_AND_VIEWS);
            ft.replace(R.id.stats_visitors_and_views_container, fragment, StatsVisitorsAndViewsFragment.TAG);
        }
        
        if (fm.findFragmentByTag(StatsClicksFragment.TAG) == null) {
            fragment = StatsAbsViewFragment.newInstance(StatsViewType.CLICKS);
            ft.replace(R.id.stats_clicks_container, fragment, StatsClicksFragment.TAG);
        }
        
        if (fm.findFragmentByTag(StatsCommentsFragment.TAG) == null) {
            fragment = StatsAbsViewFragment.newInstance(StatsViewType.COMMENTS);
            ft.replace(R.id.stats_comments_container, fragment, StatsCommentsFragment.TAG);
        }

        if (fm.findFragmentByTag(StatsGeoviewsFragment.TAG) == null) {
            fragment = StatsAbsViewFragment.newInstance(StatsViewType.VIEWS_BY_COUNTRY);
            ft.replace(R.id.stats_geoviews_container, fragment, StatsGeoviewsFragment.TAG);
        }

        if (fm.findFragmentByTag(StatsSearchEngineTermsFragment.TAG) == null) {
            fragment = StatsAbsViewFragment.newInstance(StatsViewType.SEARCH_ENGINE_TERMS);
            ft.replace(R.id.stats_searchengine_container, fragment, StatsSearchEngineTermsFragment.TAG);
        }

        if (fm.findFragmentByTag(StatsTagsAndCategoriesFragment.TAG) == null) {
            fragment = StatsAbsViewFragment.newInstance(StatsViewType.TAGS_AND_CATEGORIES);
            ft.replace(R.id.stats_tags_and_categories_container, fragment, StatsTagsAndCategoriesFragment.TAG);
        }

        if (fm.findFragmentByTag(StatsTopAuthorsFragment.TAG) == null) {
            fragment = StatsAbsViewFragment.newInstance(StatsViewType.TOP_AUTHORS);
            ft.replace(R.id.stats_top_authors_container, fragment, StatsTopAuthorsFragment.TAG);
        }

        if (fm.findFragmentByTag(StatsTotalsFollowersAndSharesFragment.TAG) == null) {
            fragment = StatsAbsViewFragment.newInstance(StatsViewType.TOTALS_FOLLOWERS_AND_SHARES);
            ft.replace(R.id.stats_totals_followers_shares_container, fragment, StatsTotalsFollowersAndSharesFragment.TAG);
        }

        if (fm.findFragmentByTag(StatsTopPostsAndPagesFragment.TAG) == null) {
            fragment = StatsAbsViewFragment.newInstance(StatsViewType.TOP_POSTS_AND_PAGES);
            ft.replace(R.id.stats_top_posts_container, fragment, StatsTopPostsAndPagesFragment.TAG);
        }

        if (fm.findFragmentByTag(StatsVideoFragment.TAG) == null) {
            fragment = StatsAbsViewFragment.newInstance(StatsViewType.VIDEO_PLAYS);
            ft.replace(R.id.stats_video_container, fragment, StatsVideoFragment.TAG);
        }

        if (fm.findFragmentByTag(StatsReferrersFragment.TAG) == null) {
            fragment = StatsReferrersFragment.newInstance(StatsViewType.REFERRERS);
            ft.replace(R.id.stats_referrers_container, fragment, StatsReferrersFragment.TAG);
        }
        
        ft.commit();
        
        if (Utils.getSmallestWidthDP() >= TABLET_720DP || (Utils.getSmallestWidthDP() == TABLET_600DP && isInLandscape()))
            loadSplitLayout();
        
    }

    private boolean isInLandscape() {
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        if (point.y < point.x) {
            return true;
        } else {
            return false;
        }
    }

    private void loadSplitLayout() {
        FrameLayout frameView;

        frameView = (FrameLayout) findViewById(R.id.stats_geoviews_container);
        mFragmentContainer.removeView(frameView);
        mColumnLeft.addView(frameView);

        frameView = (FrameLayout) findViewById(R.id.stats_totals_followers_shares_container);
        mFragmentContainer.removeView(frameView);
        mColumnLeft.addView(frameView);

        frameView = (FrameLayout) findViewById(R.id.stats_referrers_container);
        mFragmentContainer.removeView(frameView);
        mColumnLeft.addView(frameView);

        frameView = (FrameLayout) findViewById(R.id.stats_top_authors_container);
        mFragmentContainer.removeView(frameView);
        mColumnLeft.addView(frameView);

        frameView = (FrameLayout) findViewById(R.id.stats_video_container);
        mFragmentContainer.removeView(frameView);
        mColumnLeft.addView(frameView);

        frameView = (FrameLayout) findViewById(R.id.stats_top_posts_container);
        mFragmentContainer.removeView(frameView);
        mColumnRight.addView(frameView);

        frameView = (FrameLayout) findViewById(R.id.stats_comments_container);
        mFragmentContainer.removeView(frameView);
        mColumnRight.addView(frameView);
        
        frameView = (FrameLayout) findViewById(R.id.stats_clicks_container);
        mFragmentContainer.removeView(frameView);
        mColumnRight.addView(frameView);

        frameView = (FrameLayout) findViewById(R.id.stats_tags_and_categories_container);
        mFragmentContainer.removeView(frameView);
        mColumnRight.addView(frameView);

        frameView = (FrameLayout) findViewById(R.id.stats_searchengine_container);
        mFragmentContainer.removeView(frameView);
        mColumnRight.addView(frameView);
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.stats, menu);
        return true;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        refreshStatsFromServer();
    }
    

    private void refreshStatsFromServer() {
        if (WordPress.getCurrentBlog() == null)
            return; 
        
        final String blogId = String.valueOf(WordPress.getCurrentBlog().getBlogId());
        
        WordPress.restClient.getStatsSummary(blogId, 
                new Listener() {
                    
                    @Override
                    public void onResponse(JSONObject response) {
                        StatUtils.saveSummary(blogId, response);
                        StatUtils.broadcastSummaryUpdated(StatsActivityTablet.this);
                    }
                }, 
                new ErrorListener() {
                    
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        
                    }
                });
    }
    
}
