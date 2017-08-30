package kenticocloud.kenticoclouddancinggoat.app.articles;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kenticocloud.kenticoclouddancinggoat.R;
import kenticocloud.kenticoclouddancinggoat.app.article_detail.ArticleDetailActivity;
import kenticocloud.kenticoclouddancinggoat.app.core.BaseFragment;
import kenticocloud.kenticoclouddancinggoat.app.shared.CommunicationHub;
import kenticocloud.kenticoclouddancinggoat.app.shared.ScrollChildSwipeRefreshLayout;
import kenticocloud.kenticoclouddancinggoat.data.models.Article;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by RichardS on 15. 8. 2017.
 */

public class ArticlesFragment extends BaseFragment<ArticlesContract.Presenter> implements ArticlesContract.View{

    private ArticlesAdapter _adapter;

    public ArticlesFragment() {
        // Requires empty public constructor
    }

    @Override
    protected int getFragmentId(){
        return R.layout.articles_frag;
    }

    @Override
    protected int getViewId(){
        return R.id.articlesLL;
    }

    @Override
    protected boolean hasScrollSwipeRefresh() {
        return true;
    }

    @Override
    protected void onScrollSwipeRefresh() {
        _presenter.loadArticles();
    }

    @Override
    protected View scrollUpChildView() {
        return _root.findViewById(R.id.articlesLV);
    }

    public static ArticlesFragment newInstance() {
        return new ArticlesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _adapter = new ArticlesAdapter(new ArrayList<Article>(0), articleItemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ListView listView = (ListView) _root.findViewById(R.id.articlesLV);
        listView.setAdapter(_adapter);

        return _root;
    }

    @Override
    public void showArticles(List<Article> articles) {
        _adapter.replaceData(articles);
        _fragmentView.setVisibility(View.VISIBLE);
    }

    /**
     * Listener for clicks on items in the ListView.
     */
    ArticleItemListener articleItemListener = new ArticleItemListener() {
        @Override
        public void onArticleClick(Article clickedArticle) {
            Intent articleDetailIntent = new Intent(getContext(), ArticleDetailActivity.class);
            articleDetailIntent.putExtra(CommunicationHub.ArticleCodename.toString(), clickedArticle.getSystem().getCodename());
            startActivity(articleDetailIntent);
        }
    };

    private static class ArticlesAdapter extends BaseAdapter {

        private List<Article> _articles;
        private ArticleItemListener _articleItemListener;

        ArticlesAdapter(List<Article> articles, ArticleItemListener itemListener) {
            setList(articles);
            _articleItemListener = itemListener;
        }

        void replaceData(List<Article> articles) {
            setList(articles);
            notifyDataSetChanged();
        }

        private void setList(List<Article> articles) {
            _articles = checkNotNull(articles);
        }

        @Override
        public int getCount() {
            return _articles.size();
        }

        @Override
        public Article getItem(int i) {
            return _articles.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.article_item, viewGroup, false);
            }

            final Article article = getItem(i);

            // title
            TextView titleTV = (TextView) rowView.findViewById(R.id.articleTitleTV);
            titleTV.setText(article.getTitle());

            // image
            final ImageView teaserIV = (ImageView) rowView.findViewById(R.id.articleTeaserIV);
            Picasso.with(viewGroup.getContext()).load(article.getTeaserImageUrl()).into(teaserIV);

            // release date
            TextView postDateTV = (TextView) rowView.findViewById(R.id.articlePostDateTV);
            SimpleDateFormat postDf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            postDateTV.setText(postDf.format(article.getPostDate()));

            // summary
            TextView summaryTV = (TextView) rowView.findViewById(R.id.articleSummaryTV);
            summaryTV.setText(article.getSummary());

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _articleItemListener.onArticleClick(article);
                }
            });

            return rowView;
        }
    }

    interface ArticleItemListener {

        void onArticleClick(Article clickedArticle);
    }
}
