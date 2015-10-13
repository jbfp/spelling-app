package dk.jbfp.staveapp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dk.jbfp.staveapp.R;
import dk.jbfp.staveapp.User;
import dk.jbfp.staveapp.UserRepositoryImpl;
import dk.jbfp.staveapp.level.LevelActivity;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private UserAdapter userAdapter;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userAdapter = new UserAdapter(this, new ArrayList<User>());
        GridView gridView = (GridView) findViewById(R.id.users_view);
        gridView.setAdapter(userAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onUserClicked(userAdapter.getItem(position));
            }
        });

        this.presenter = new LoginPresenter(new UserRepositoryImpl());
        this.presenter.setView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_user:
                this.presenter.onNewUserClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showUsers(List<User> users) {
        this.userAdapter.setUsers(users);
    }

    @Override
    public void navigateToRegisterActivity() {
        Intent intent = new Intent(this, LevelActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToStartActivity(int userId) {
        Intent intent = new Intent(this, LevelActivity.class);
        intent.putExtra(LevelActivity.USER_ID_KEY, userId);
        startActivity(intent);
    }

    private class UserAdapter extends BaseAdapter {
        private final Context context;
        private final List<User> users;

        private UserAdapter(Context context, List<User> users) {
            this.context = context;
            this.users = users;
        }

        public void setUsers(List<User> users) {
            this.users.clear();
            this.users.addAll(users);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.users.size();
        }

        @Override
        public User getItem(int position) {
            return this.users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.layout_user_item, null);
            }

            ImageView profilePictureView = (ImageView) view.findViewById(R.id.user_picture);
            profilePictureView.setImageResource(android.R.drawable.ic_delete);

            TextView userNameView = (TextView) view.findViewById(R.id.user_name_view);
            userNameView.setText(this.users.get(position).name);

            return view;
        }
    }
}

