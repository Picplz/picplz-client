package com.hm.picplz.ui.screen.main.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.feature.main.R as FeatureMainR

private object SearchFieldDefaults {
    val HorizontalPadding = 16.dp
    val VerticalPadding = 10.dp
    val CornerRadius = 50.dp
    val StrokeWidth = 1.dp
    val TextLineHeight = 20.sp
    val ClearButtonSize = 20.dp
    val ClearIconSize = 13.dp
    val IconSize = 20.dp
    val IconGap = 10.dp
    val TextIconGap = 8.dp
}

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "검색어를 입력하세요",
    onSearchClick: (() -> Unit)? = null,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Search,
    keyboardActions: (() -> Unit)? = null,
    autoFocus: Boolean = false,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(SearchFieldDefaults.CornerRadius))
                .border(
                    width = SearchFieldDefaults.StrokeWidth,
                    color = MainThemeColor.Gray6,
                    shape = RoundedCornerShape(SearchFieldDefaults.CornerRadius),
                )
                .padding(
                    horizontal = SearchFieldDefaults.HorizontalPadding,
                    vertical = SearchFieldDefaults.VerticalPadding,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(end = SearchFieldDefaults.TextIconGap)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            onFocusChanged?.invoke(focusState.isFocused)
                        },
                enabled = enabled,
                textStyle =
                    MainThemeFont.Body.copy(
                        color = MainThemeColor.Black,
                        lineHeight = SearchFieldDefaults.TextLineHeight,
                    ),
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = imeAction,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onSearch = {
                            keyboardActions?.invoke()
                            focusManager.clearFocus()
                        },
                        onDone = {
                            keyboardActions?.invoke()
                            focusManager.clearFocus()
                        },
                    ),
                cursorBrush = SolidColor(MainThemeColor.Black),
                singleLine = true,
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MainThemeFont.Body.copy(lineHeight = SearchFieldDefaults.TextLineHeight),
                            color = MainThemeColor.Gray3,
                        )
                    }
                    inner()
                },
            )

            if (value.isNotEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .size(SearchFieldDefaults.ClearButtonSize)
                            .clip(CircleShape)
                            .background(MainThemeColor.Gray2)
                            .clickable {
                                onValueChange("")
                                focusManager.clearFocus()
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription =
                            stringResource(FeatureMainR.string.main_search_clear_content_description),
                        Modifier.size(SearchFieldDefaults.ClearIconSize),
                        tint = MainThemeColor.Gray4,
                    )
                }
            }

            Spacer(modifier = Modifier.width(SearchFieldDefaults.IconGap))

            Icon(
                painter = painterResource(id = FeatureMainR.drawable.main_search_icon),
                contentDescription = stringResource(FeatureMainR.string.main_search_icon_content_description),
                modifier =
                    Modifier
                        .size(SearchFieldDefaults.IconSize)
                        .clickable {
                            onSearchClick?.invoke()
                                ?: keyboardActions?.invoke()
                            focusManager.clearFocus()
                        },
                tint = MainThemeColor.Gray6,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchFieldPreview() {
    PicplzTheme {
        SearchField(
            value = "",
            onValueChange = {},
            placeholder = "동명(동, 면)으로 검색 (ex, 연남동)",
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchFieldWithValuePreview() {
    PicplzTheme {
        SearchField(
            value = "연남동",
            onValueChange = {},
            placeholder = "동명(동, 면)으로 검색 (ex, 연남동)",
            modifier = Modifier.padding(16.dp),
        )
    }
}
