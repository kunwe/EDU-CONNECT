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
public final class QuizViewModel_Factory implements Factory<QuizViewModel> {
  private final Provider<QuizRepository> quizRepositoryProvider;

  public QuizViewModel_Factory(Provider<QuizRepository> quizRepositoryProvider) {
    this.quizRepositoryProvider = quizRepositoryProvider;
  }

  @Override
  public QuizViewModel get() {
    return newInstance(quizRepositoryProvider.get());
  }

  public static QuizViewModel_Factory create(Provider<QuizRepository> quizRepositoryProvider) {
    return new QuizViewModel_Factory(quizRepositoryProvider);
  }

  public static QuizViewModel newInstance(QuizRepository quizRepository) {
    return new QuizViewModel(quizRepository);
  }
}
