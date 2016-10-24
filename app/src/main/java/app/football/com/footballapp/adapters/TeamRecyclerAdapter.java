package app.football.com.footballapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.football.com.footballapp.R;
import app.football.com.footballapp.model.FootballTeam;

/**
 * Created by cognitive on 10/24/16.
 */
public class TeamRecyclerAdapter extends RecyclerView.Adapter<TeamRecyclerAdapter.ViewHolder> {

    private ArrayList<FootballTeam> mFootballTeams;
    public TeamRecyclerAdapter( ArrayList<FootballTeam> footballTeams ) {
        this.mFootballTeams = footballTeams;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FootballTeam footballTeam = mFootballTeams.get(position);
        holder.descriptionTextView.setText(footballTeam.getDescriptionText(position));
    }

    @Override
    public int getItemCount() {
        if( mFootballTeams == null ) {
            return 0;
        }
        return mFootballTeams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView descriptionTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
