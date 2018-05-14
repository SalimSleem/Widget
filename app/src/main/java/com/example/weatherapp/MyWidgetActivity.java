package com.example.weatherapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.SystemClock;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.weatherapp.R;
import com.example.weatherapp.api.weather.WeatherApiManager;
import com.example.weatherapp.models.CurrentWeatherInfo;
import com.example.weatherapp.models.ForecastWeatherInfo;
import com.example.weatherapp.models.ForecastWeatherItem;
import com.example.weatherapp.models.TempInfo;
import com.example.weatherapp.models.WeatherDescription;
import com.example.weatherapp.screens.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.weatherapp.models.CurrentWeatherInfo;
import com.example.weatherapp.models.WeatherDescription;
import com.squareup.picasso.Picasso;

import static android.content.Context.ALARM_SERVICE;
import static android.provider.Settings.Global.getString;
import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link MyWidgetActivityConfigureActivity MyWidgetActivityConfigureActivity}
 */
public class MyWidgetActivity extends AppWidgetProvider {


    private WeatherApiManager weatherApiManager;
    private Double latitude, longitude;
    private LocationManager locationManager;
    private ImageView currentConditionImageView;
    private TextView currentTemperatureTextView, weatherDescriptionTextView;
    private LottieAnimationView loadingAnimationView;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            locationManager.removeUpdates(locationListener);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            weatherApiManager.getWeatherAtCoordinates(33.9, 35.5);
            weatherApiManager.getForecastAtCoordinates(latitude, longitude);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }


    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int mAppWidgetId) {
    }

    class ForecastListAdapter extends RecyclerView.Adapter<MainActivity.ForecastListAdapter.ViewHolder> {

        private List<ForecastWeatherItem> items;

        public ForecastListAdapter(List<ForecastWeatherItem> items) {
            this.items = items;
        }

        @Override
        public MainActivity.ForecastListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
            return new MainActivity.ForecastListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MainActivity.ForecastListAdapter.ViewHolder holder, int position) {
            Context context = holder.itemView.getContext();
            TempInfo tempInfo = items.get(position).getTempInfo();
            String currentTemp = String.valueOf(tempInfo.getTemp());
            currentTemp = String.format(context.getString(R.string.current_temp), currentTemp);
            holder.currentTempTextView.setText(currentTemp);


            List<WeatherDescription> descriptions = items.get(position).getWeatherDescriptions();
            if (descriptions != null && !descriptions.isEmpty()) {
                WeatherDescription description = descriptions.get(0);
                String desc = description.getDescription();
                holder.descriptionTextView.setText(desc);

                String iconUrl = "http://openweathermap.org/img/w/" + description.getIcon() + ".png";
                Picasso.with(context).load(iconUrl).into(holder.currentWeatherImageView);
            }
        }

        private void showCurrentWeather(CurrentWeatherInfo info) {
            String currentTemp = String.valueOf(info.getTempInfo().getTemp());
            currentTemp = String.format(getString(R.string.current_temp), currentTemp);
            currentTemperatureTextView.setText(currentTemp);


            if (info.getWeatherDescriptions() != null && !info.getWeatherDescriptions().isEmpty()) {
                WeatherDescription currentWeatherDescription = info.getWeatherDescriptions().get(0);

                String weatherDescription = currentWeatherDescription.getDescription();
                weatherDescriptionTextView.setText(weatherDescription);

                String iconUrl = "http://openweathermap.org/img/w/" + currentWeatherDescription.getIcon() + ".png";

            }
        }

        private String getString(int current_temp) {
            return null;
        }


        @Override
        public int getItemCount() {
            return items.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView currentWeatherImageView;
            TextView currentTempTextView;
            TextView descriptionTextView;
            RelativeLayout widget;

            public ViewHolder(View itemView) {
                super(itemView);
                currentWeatherImageView = itemView.findViewById(R.id.weather_condition);
                currentTempTextView = itemView.findViewById(R.id.current_temp);
                descriptionTextView = itemView.findViewById(R.id.weather_description);
                widget = itemView.findViewById(R.id.weather_widget);

            }
        }

    }



        private void getForecastAtCoordinates(double latitude, double longitude) {
            weatherApiManager.getForecastWeatherInfo(latitude, longitude).enqueue(new Callback<ForecastWeatherInfo>() {
                @Override
                public void onResponse(Call<ForecastWeatherInfo> call, Response<ForecastWeatherInfo> response) {
                    if (response.isSuccessful()) {
                        ForecastWeatherInfo forecastWeatherInfo = response.body();
                        if (forecastWeatherInfo != null) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<ForecastWeatherInfo> call, Throwable t) {

                }

            });
        }
};

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int mAppWidgetId) {

    }
}

