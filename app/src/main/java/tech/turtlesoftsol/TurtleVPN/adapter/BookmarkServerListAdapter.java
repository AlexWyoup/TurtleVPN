package tech.turtlesoftsol.TurtleVPN.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import tech.turtlesoftsol.TurtleVPN.activity.ServerActivity;
import tech.turtlesoftsol.TurtleVPN.database.DBHelper;
import tech.turtlesoftsol.TurtleVPN.model.Server;
import tech.turtlesoftsol.TurtleVPN.util.ConnectionQuality;
import tech.turtlesoftsol.TurtleVPN.util.CountriesNames;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kusenko on 22.01.2017.
 */

public class BookmarkServerListAdapter extends RecyclerView.Adapter<BookmarkServerListAdapter.BookmarkHolder>{

    private List<Server> serverList = new ArrayList<Server>();
    private Context context;
    private Map<String, String> localeCountries;
    private DBHelper dbHelper;

    public BookmarkServerListAdapter(List<Server> serverList, Context context, DBHelper dbHelper) {
        this.serverList = serverList;
        this.context = context;
        this.dbHelper = dbHelper;
        localeCountries = CountriesNames.getCountries();
    }

    @Override
    public BookmarkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(tech.turtlesoftsol.TurtleVPN.R.layout.bookmark_server_row, parent, false);

        return new BookmarkHolder(v);
    }

    @Override
    public void onBindViewHolder(BookmarkHolder holder, final int position) {
        final Server server = serverList.get(position);

        String code = server.getCountryShort().toLowerCase();
        if (code.equals("do"))
            code = "dom";

        holder.flag.setImageResource(
                context.getResources().getIdentifier(code,
                        "drawable",
                        context.getPackageName()));
        holder.quality.setImageResource(
                context.getResources().getIdentifier(ConnectionQuality.getConnectIcon(server.getQuality()),
                        "drawable",
                        context.getPackageName()));
        holder.host.setText(server.getHostName());
        holder.ip.setText(server.getIp());
        holder.city.setText(server.getCity());

        String localeCountryName = localeCountries.get(server.getCountryShort()) != null ?
                localeCountries.get(server.getCountryShort()) : server.getCountryLong();
        holder.country.setText(localeCountryName);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.delBookmark(server);
                serverList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return serverList.size();
    }

    class BookmarkHolder extends RecyclerView.ViewHolder {
        ImageView flag;
        ImageView quality;
        TextView host;
        TextView ip;
        TextView city;
        TextView country;
        Button delete;

        public BookmarkHolder(View v) {
            super(v);
            flag = (ImageView) v.findViewById(tech.turtlesoftsol.TurtleVPN.R.id.bookmarkFlag);
            quality = (ImageView) v.findViewById(tech.turtlesoftsol.TurtleVPN.R.id.bookmarkConnect);
            host = (TextView) v.findViewById(tech.turtlesoftsol.TurtleVPN.R.id.bookmarkHostName);
            ip = (TextView) v.findViewById(tech.turtlesoftsol.TurtleVPN.R.id.bookmarkIP);
            city = (TextView) v.findViewById(tech.turtlesoftsol.TurtleVPN.R.id.bookmarkCity);
            country = (TextView) v.findViewById(tech.turtlesoftsol.TurtleVPN.R.id.bookmarkCountry);
            delete = (Button) v.findViewById(tech.turtlesoftsol.TurtleVPN.R.id.bookmarkDelete);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ServerActivity.class);
                    intent.putExtra(Server.class.getCanonicalName(), serverList.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
