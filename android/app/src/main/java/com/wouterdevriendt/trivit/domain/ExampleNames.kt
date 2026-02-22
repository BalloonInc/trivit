package com.wouterdevriendt.trivit.domain

import android.content.Context
import com.wouterdevriendt.trivit.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExampleNames @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val nameResIds = listOf(
        // Healthy habits
        R.string.example_glasses_of_water,
        R.string.example_fruits_eaten,
        R.string.example_vegetables,
        R.string.example_hours_sleep,
        R.string.example_steps_walked,
        R.string.example_pushups,
        R.string.example_minutes_exercised,
        R.string.example_healthy_snacks,
        R.string.example_deep_breaths,
        // Fun & silly
        R.string.example_said_wow,
        R.string.example_cookies,
        R.string.example_silly_dances,
        R.string.example_dad_jokes,
        R.string.example_pillows,
        R.string.example_socks,
        R.string.example_clouds,
        R.string.example_dog_tail,
        R.string.example_sneezes,
        R.string.example_yawns,
        R.string.example_high_fives,
        R.string.example_bubbles,
        R.string.example_puddles,
        R.string.example_funny_faces,
        R.string.example_shower_songs,
        R.string.example_forgot_room,
        R.string.example_dinosaur,
        R.string.example_cards,
        R.string.example_grapes,
        R.string.example_paper_airplanes,
        // Productivity
        R.string.example_books_read,
        R.string.example_pages,
        R.string.example_tasks,
        R.string.example_ideas,
        R.string.example_problems,
        R.string.example_focus,
        R.string.example_emails,
        R.string.example_meetings,
        // Kindness & gratitude
        R.string.example_hugs,
        R.string.example_thank_yous,
        R.string.example_smiles,
        R.string.example_compliments,
        R.string.example_kindness,
        R.string.example_friends,
        R.string.example_doors,
        // Learning
        R.string.example_new_words,
        R.string.example_questions,
        R.string.example_facts,
        R.string.example_languages,
        R.string.example_puzzles
    )

    fun random(): String = context.getString(nameResIds.random())

    fun getAll(): List<String> = nameResIds.map { context.getString(it) }
}
