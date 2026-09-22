package com.example.educonnect;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DaggerEduConnectApp_HiltComponents_SingletonC {
  private DaggerEduConnectApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static EduConnectApp_HiltComponents.SingletonC create() {
    return new Builder().build();
  }

  public static final class Builder {
    private Builder() {
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public EduConnectApp_HiltComponents.SingletonC build() {
      return new SingletonCImpl();
    }
  }

  private static final class ActivityRetainedCBuilder implements EduConnectApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public EduConnectApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements EduConnectApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public EduConnectApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements EduConnectApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public EduConnectApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements EduConnectApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public EduConnectApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements EduConnectApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public EduConnectApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements EduConnectApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public EduConnectApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements EduConnectApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public EduConnectApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends EduConnectApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends EduConnectApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public void injectHomeFragment(HomeFragment homeFragment) {
    }

    @Override
    public void injectQuizzesFragment(QuizzesFragment quizzesFragment) {
    }

    @Override
    public void injectSettingsFragment(SettingsFragment settingsFragment) {
    }

    @Override
    public void injectSubjectsFragment(SubjectsFragment subjectsFragment) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends EduConnectApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends EduConnectApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectChatActivity(ChatActivity chatActivity) {
    }

    @Override
    public void injectHomeActivity(HomeActivity homeActivity) {
    }

    @Override
    public void injectLoginActivity(LoginActivity loginActivity) {
    }

    @Override
    public void injectMainContainerActivity(MainContainerActivity mainContainerActivity) {
    }

    @Override
    public void injectQuizActivity(QuizActivity quizActivity) {
    }

    @Override
    public void injectQuizHistoryActivity(QuizHistoryActivity quizHistoryActivity) {
    }

    @Override
    public void injectRegisterActivity(RegisterActivity registerActivity) {
    }

    @Override
    public void injectSummaryActivity(SummaryActivity summaryActivity) {
    }

    @Override
    public void injectSummaryHistoryActivity(SummaryHistoryActivity summaryHistoryActivity) {
    }

    @Override
    public void injectTimetableActivity(TimetableActivity timetableActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>of(LazyClassKeyProvider.com_example_educonnect_AuthViewModel, AuthViewModel_HiltModules.KeyModule.provide(), LazyClassKeyProvider.com_example_educonnect_ChatViewModel, ChatViewModel_HiltModules.KeyModule.provide(), LazyClassKeyProvider.com_example_educonnect_QuizViewModel, QuizViewModel_HiltModules.KeyModule.provide(), LazyClassKeyProvider.com_example_educonnect_SummaryViewModel, SummaryViewModel_HiltModules.KeyModule.provide(), LazyClassKeyProvider.com_example_educonnect_TimetableViewModel, TimetableViewModel_HiltModules.KeyModule.provide()));
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_example_educonnect_QuizViewModel = "com.example.educonnect.QuizViewModel";

      static String com_example_educonnect_TimetableViewModel = "com.example.educonnect.TimetableViewModel";

      static String com_example_educonnect_ChatViewModel = "com.example.educonnect.ChatViewModel";

      static String com_example_educonnect_SummaryViewModel = "com.example.educonnect.SummaryViewModel";

      static String com_example_educonnect_AuthViewModel = "com.example.educonnect.AuthViewModel";

      @KeepFieldType
      QuizViewModel com_example_educonnect_QuizViewModel2;

      @KeepFieldType
      TimetableViewModel com_example_educonnect_TimetableViewModel2;

      @KeepFieldType
      ChatViewModel com_example_educonnect_ChatViewModel2;

      @KeepFieldType
      SummaryViewModel com_example_educonnect_SummaryViewModel2;

      @KeepFieldType
      AuthViewModel com_example_educonnect_AuthViewModel2;
    }
  }

  private static final class ViewModelCImpl extends EduConnectApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<ChatViewModel> chatViewModelProvider;

    private Provider<QuizViewModel> quizViewModelProvider;

    private Provider<SummaryViewModel> summaryViewModelProvider;

    private Provider<TimetableViewModel> timetableViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.chatViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.quizViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.summaryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.timetableViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>of(LazyClassKeyProvider.com_example_educonnect_AuthViewModel, ((Provider) authViewModelProvider), LazyClassKeyProvider.com_example_educonnect_ChatViewModel, ((Provider) chatViewModelProvider), LazyClassKeyProvider.com_example_educonnect_QuizViewModel, ((Provider) quizViewModelProvider), LazyClassKeyProvider.com_example_educonnect_SummaryViewModel, ((Provider) summaryViewModelProvider), LazyClassKeyProvider.com_example_educonnect_TimetableViewModel, ((Provider) timetableViewModelProvider)));
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_example_educonnect_ChatViewModel = "com.example.educonnect.ChatViewModel";

      static String com_example_educonnect_QuizViewModel = "com.example.educonnect.QuizViewModel";

      static String com_example_educonnect_TimetableViewModel = "com.example.educonnect.TimetableViewModel";

      static String com_example_educonnect_SummaryViewModel = "com.example.educonnect.SummaryViewModel";

      static String com_example_educonnect_AuthViewModel = "com.example.educonnect.AuthViewModel";

      @KeepFieldType
      ChatViewModel com_example_educonnect_ChatViewModel2;

      @KeepFieldType
      QuizViewModel com_example_educonnect_QuizViewModel2;

      @KeepFieldType
      TimetableViewModel com_example_educonnect_TimetableViewModel2;

      @KeepFieldType
      SummaryViewModel com_example_educonnect_SummaryViewModel2;

      @KeepFieldType
      AuthViewModel com_example_educonnect_AuthViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.educonnect.AuthViewModel 
          return (T) new AuthViewModel(singletonCImpl.authRepositoryProvider.get());

          case 1: // com.example.educonnect.ChatViewModel 
          return (T) new ChatViewModel(singletonCImpl.chatRepositoryProvider.get());

          case 2: // com.example.educonnect.QuizViewModel 
          return (T) new QuizViewModel(singletonCImpl.quizRepositoryProvider.get());

          case 3: // com.example.educonnect.SummaryViewModel 
          return (T) new SummaryViewModel(singletonCImpl.summaryRepositoryProvider.get());

          case 4: // com.example.educonnect.TimetableViewModel 
          return (T) new TimetableViewModel(singletonCImpl.timetableRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends EduConnectApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends EduConnectApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends EduConnectApp_HiltComponents.SingletonC {
    private final SingletonCImpl singletonCImpl = this;

    private Provider<FirebaseAuth> provideFirebaseAuthProvider;

    private Provider<FirebaseFirestore> provideFirebaseFirestoreProvider;

    private Provider<AuthRepository> authRepositoryProvider;

    private Provider<GeminiApi> provideGeminiApiProvider;

    private Provider<ChatRepository> chatRepositoryProvider;

    private Provider<QuizRepository> quizRepositoryProvider;

    private Provider<SummaryRepository> summaryRepositoryProvider;

    private Provider<TimetableRepository> timetableRepositoryProvider;

    private SingletonCImpl() {

      initialize();

    }

    @SuppressWarnings("unchecked")
    private void initialize() {
      this.provideFirebaseAuthProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseAuth>(singletonCImpl, 1));
      this.provideFirebaseFirestoreProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseFirestore>(singletonCImpl, 2));
      this.authRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 0));
      this.provideGeminiApiProvider = DoubleCheck.provider(new SwitchingProvider<GeminiApi>(singletonCImpl, 4));
      this.chatRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ChatRepository>(singletonCImpl, 3));
      this.quizRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<QuizRepository>(singletonCImpl, 5));
      this.summaryRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SummaryRepository>(singletonCImpl, 6));
      this.timetableRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<TimetableRepository>(singletonCImpl, 7));
    }

    @Override
    public void injectEduConnectApp(EduConnectApp eduConnectApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.educonnect.AuthRepository 
          return (T) new AuthRepository(singletonCImpl.provideFirebaseAuthProvider.get(), singletonCImpl.provideFirebaseFirestoreProvider.get());

          case 1: // com.google.firebase.auth.FirebaseAuth 
          return (T) FirebaseModule_ProvideFirebaseAuthFactory.provideFirebaseAuth();

          case 2: // com.google.firebase.firestore.FirebaseFirestore 
          return (T) FirebaseModule_ProvideFirebaseFirestoreFactory.provideFirebaseFirestore();

          case 3: // com.example.educonnect.ChatRepository 
          return (T) new ChatRepository(singletonCImpl.provideGeminiApiProvider.get(), singletonCImpl.provideFirebaseFirestoreProvider.get(), singletonCImpl.provideFirebaseAuthProvider.get());

          case 4: // com.example.educonnect.GeminiApi 
          return (T) NetworkModule_ProvideGeminiApiFactory.provideGeminiApi();

          case 5: // com.example.educonnect.QuizRepository 
          return (T) new QuizRepository(singletonCImpl.provideGeminiApiProvider.get(), singletonCImpl.provideFirebaseFirestoreProvider.get(), singletonCImpl.provideFirebaseAuthProvider.get());

          case 6: // com.example.educonnect.SummaryRepository 
          return (T) new SummaryRepository(singletonCImpl.provideGeminiApiProvider.get(), singletonCImpl.provideFirebaseFirestoreProvider.get(), singletonCImpl.provideFirebaseAuthProvider.get());

          case 7: // com.example.educonnect.TimetableRepository 
          return (T) new TimetableRepository(singletonCImpl.provideGeminiApiProvider.get(), singletonCImpl.provideFirebaseFirestoreProvider.get(), singletonCImpl.provideFirebaseAuthProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
