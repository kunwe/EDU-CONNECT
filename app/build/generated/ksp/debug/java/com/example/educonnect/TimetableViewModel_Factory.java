package com.example.educonnect;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
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
public final class TimetableViewModel_Factory implements Factory<TimetableViewModel> {
  private final Provider<TimetableRepository> repositoryProvider;

  public TimetableViewModel_Factory(Provider<TimetableRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public TimetableViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static TimetableViewModel_Factory create(
      Provider<TimetableRepository> repositoryProvider) {
    return new TimetableViewModel_Factory(repositoryProvider);
  }

  public static TimetableViewModel newInstance(TimetableRepository repository) {
    return new TimetableViewModel(repository);
  }
}
