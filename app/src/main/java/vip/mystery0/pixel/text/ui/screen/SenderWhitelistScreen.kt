package vip.mystery0.pixel.text.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import vip.mystery0.pixel.text.ui.component.RuleEmptyState
import vip.mystery0.pixel.text.ui.component.RuleHelperCard
import vip.mystery0.pixel.text.ui.component.RuleItemCard
import vip.mystery0.pixel.text.viewmodel.SenderWhitelistViewModel

@Composable
fun SenderWhitelistScreen(
    onNavigateBack: () -> Unit,
    viewModel: SenderWhitelistViewModel = koinViewModel(),
) {
    val rules by viewModel.rules.collectAsState()
    val state by viewModel.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    BackHandler(enabled = state.busy) { }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("骚扰识别白名单") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, enabled = !state.busy) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier.navigationBarsPadding(),
                onClick = { viewModel.openEditor() },
                icon = { Icon(Icons.Rounded.Add, contentDescription = null) },
                text = { Text("添加规则") },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().consumeWindowInsets(padding),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp,
                top = padding.calculateTopPadding() + 12.dp, bottom = 104.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(key = "privacy") {
                RuleHelperCard(
                    icon = Icons.Rounded.Shield,
                    title = "规则仅保存在本机",
                    description = "命中号码的已有和新消息都按非骚扰处理，不执行骚扰静默、隔离或自动操作。\n\n删除规则不会撤销已放行消息，也不会恢复此前已删除的消息。",
                )
            }
            if (rules.isEmpty()) {
                item(key = "empty") {
                    RuleEmptyState(
                        icon = Icons.Rounded.VerifiedUser,
                        title = "暂无白名单规则",
                        description = "添加后，命中规则的号码将直接放行，不执行骚扰拦截。",
                    )
                }
            } else {
                item(key = "count") {
                    Text(
                        "已添加 ${rules.size} 条规则",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 4.dp),
                    )
                }
            }
            items(rules, key = { it.id }) { rule ->
                RuleItemCard(
                    title = rule.value,
                    subtitle = rule.type.label,
                    onEdit = { viewModel.openEditor(rule) },
                    onDelete = { viewModel.requestDelete(rule) },
                    enabled = !state.busy,
                    titleMaxLines = 3,
                    editDescription = "编辑规则 ${rule.value}",
                    deleteDescription = "删除规则 ${rule.value}",
                )
            }
        }
    }
    SenderWhitelistDialogs(viewModel, state, snackbar)
}

/** 会话里的快捷添加也复用编辑器和历史放行确认，不直接静默写入规则。 */
@Composable
fun ConversationWhitelistAction(
    sender: String,
    snackbar: SnackbarHostState,
    viewModel: SenderWhitelistViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    IconButton(onClick = { viewModel.openEditor(sender = sender) }, enabled = sender.isNotBlank() && !state.busy) {
        Icon(Icons.Rounded.VerifiedUser, contentDescription = "添加到骚扰识别白名单")
    }
    SenderWhitelistDialogs(viewModel, state, snackbar)
}
