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
public final class SummaryViewModel_Factory implements Factory<SummaryViewModel> {
  private final Provider<SummaryRepository> repositoryProvider;

  public SummaryViewModel_Factory(Provider<SummaryRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SummaryViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static SummaryViewModel_Factory create(Provider<SummaryRepository> repositoryProvider) {
    return new SummaryViewModel_Factory(repositoryProvider);
  }

  public static SummaryViewModel newInstance(SummaryRepository repository) {
    return new SummaryViewModel(repository);
  }
}
