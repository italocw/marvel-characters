package com.example.marvel_characters.ui.compose.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.marvel_characters.R
import com.example.marvel_characters.Samples
import com.example.marvel_characters.domain.MarvelCharacter
import com.example.marvel_characters.ui.compose.theme.MarvelCharactersTheme


@Composable
fun MarvelCharacterDetailContent(
    marvelCharacter: MarvelCharacter, modifier: Modifier = Modifier, onBackPress: () -> Unit
) {
    val smallPadding = dimensionResource(id = R.dimen.small_padding)
    val mediumPadding = dimensionResource(id = R.dimen.medium_padding)


    ConstraintLayout(
        modifier = modifier.fillMaxWidth()

    ) {
        val (buttons, image, remainingCharacterData) = createRefs()

        marvelCharacter.let {

            CharacterImage(
                modifier = Modifier
                    .padding(bottom = smallPadding)
                    .height(dimensionResource(id = R.dimen.large_character_thumbnail_height))
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                    }, thumbnailUrl = it.thumbnailUrl, name = it.name
            )

            Buttons(Modifier.constrainAs(buttons) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, onBackPress)
            Column(
                Modifier
                    .padding(
                        start = mediumPadding, end = mediumPadding, bottom = mediumPadding
                    )
                    .constrainAs(remainingCharacterData) {
                        top.linkTo(image.bottom)
                    }) {

                Name(
                    modifier = Modifier.padding(bottom = smallPadding),
                    name = it.name,
                    style = MaterialTheme.typography.titleLarge
                )
                DescriptionFull(
                    description = it.description, style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun Buttons(
    modifier: Modifier, onBackPress: () -> Unit
) {

    val buttonElevation = dimensionResource(id = R.dimen.button_elevation)
    val downloadText = stringResource(id = R.string.download)

    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (backArrow, optionsMenu) = createRefs()

        IconButton(onClick = onBackPress,
            Modifier
                .constrainAs(backArrow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }) {
            Icon(
                modifier =  Modifier
                    .background( MaterialTheme.colorScheme.surfaceColorAtElevation(buttonElevation), shape = CircleShape),

                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back_button)
            )
        }

        var expanded by remember { mutableStateOf(false) }

        IconButton(onClick = { expanded = !expanded },
            Modifier
                .constrainAs(optionsMenu) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }) {
            Icon(
                modifier =  Modifier
                    .background( MaterialTheme.colorScheme.surfaceColorAtElevation(buttonElevation), shape = CircleShape),

                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.more)
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text(downloadText) }, onClick = { })

            }
        }
    }
}


@Composable
fun NewDetailAppBar(
    onBackPressed: () -> Unit, downloadCharacter: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        IconButton(onClick = onBackPressed) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back_button)
            )
        }

        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.more)
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text(stringResource(R.string.download)) },
                onClick = { downloadCharacter() })
        }
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES, name = "DefaultPreviewDark"
)

@Composable
fun MarvelCharacterDetailItemPreview() {
    MarvelCharactersTheme {
        Surface {
            MarvelCharacterListItem(Samples.characterWithCompleteData, navigateToCharacter = { })
        }
    }
}



